## Copyright 2015 JAXIO http://www.jaxio.com
##
## Licensed under the Apache License, Version 2.0 (the "License");
## you may not use this file except in compliance with the License.
## You may obtain a copy of the License at
##
##    http://www.apache.org/licenses/LICENSE-2.0
##
## Unless required by applicable law or agreed to in writing, software
## distributed under the License is distributed on an "AS IS" BASIS,
## WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
## See the License for the specific language governing permissions and
## limitations under the License.
##
$output.skipIf($entity.isVirtual())##
$output.javaTest($entity.repository, "${entity.repository.type}IT")##

#if ($entity.root.hasSimplePk() && $entity.root.primaryKey.attribute.isDate())
$output.require("java.util.Date")##
#end
$output.require("java.util.Set")##
$output.require("javax.inject.Inject")##
$output.require("javax.persistence.EntityManager")##
$output.require("javax.persistence.PersistenceContext")##
$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.requireStatic("com.google.common.collect.Sets.newHashSet")##
$output.requireStatic("org.fest.assertions.Assertions.assertThat")##
$output.require("org.junit.Test")##
$output.require("org.junit.runner.RunWith")##
$output.require("org.springframework.test.annotation.Rollback")##
$output.require("org.springframework.test.context.ContextConfiguration")##
$output.require("org.springframework.test.context.junit4.SpringJUnit4ClassRunner")##
$output.require("org.springframework.transaction.annotation.Transactional")##
$output.require($entity.model)##
$output.require($entity.modelGenerator)##
$output.require($entity.repository)##

/**
 * Integration test on ${entity.repository.type}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext-test.xml" })
@Transactional
public class $output.currentClass {
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(${output.currentClass}.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private ${entity.repository.type} $entity.repository.var;

    @Inject
    private $entity.modelGenerator.type $entity.modelGenerator.var;

    @Test
    @Rollback
    public void saveAndGet() {
        $entity.model.type $entity.model.var = ${entity.modelGenerator.var}.${entity.model.getter}();
#if ($entity.root.hasSimplePk() && $entity.root.primaryKey.attribute.isDate())
        ${entity.model.var}.${entity.root.primaryKey.attribute.setter}(new Date());
#end

        // add it to a Set before saving (force equals/hashcode)
        Set<$entity.model.type> set = newHashSet($entity.model.var, $entity.model.var);
        assertThat(set).hasSize(1);

        ${entity.repository.var}.save(${entity.model.var});
        entityManager.flush();

        // reload it from cache and check equality
        $entity.model.type model = new ${entity.model.type}();
#if ($entity.root.primaryKey.isComposite())
        model.${entity.root.primaryKey.setter}($entity.model.var.${entity.root.primaryKey.getter}());
#elseif ($entity.root.primaryKey.isSimple())
        model.${entity.root.primaryKey.attribute.setter}($entity.model.var.${entity.root.primaryKey.attribute.getter}());
#end
        assertThat($entity.model.var).isEqualTo(${entity.repository.var}.get(model));

        // clear cache to force reload from db
        entityManager.clear();

#if ($entity.root.primaryKey.isComposite() || ($entity.root.hasSimplePk() && ($entity.root.primaryKey.attribute.jpa.isManuallyAssigned() || $entity.root.primaryKey.attribute.hasForwardXToOneRelation())) || $entity.useBusinessKey())
        // since you use a business key, equality must be preserved.
        assertThat($entity.model.var).isEqualTo( ${entity.repository.var}.get(model));
#else
        // pk are equals...
        assertThat(${entity.model.var}.${identifiableProperty.getter}()).isEqualTo(${entity.repository.var}.get(model).${identifiableProperty.getter}());

        // but since you do not use a business key, equality is lost.
        assertThat(${entity.model.var}).isNotEqualTo(${entity.repository.var}.get(model));
#end
    }
#if ($entity.root.hasSimplePk() && $entity.useBusinessKey())
$output.require("com.jaxio.jpa.querybyexample.SearchParameters")##
$output.require("org.springframework.core.serializer.DefaultDeserializer")##
$output.require("org.springframework.core.serializer.DefaultSerializer")##
$output.require("java.io.ByteArrayInputStream")##
$output.require("java.io.ByteArrayOutputStream")##
$output.requireStatic("com.jaxio.jpa.querybyexample.PropertySelector.newPropertySelector")##
$output.requireStatic("org.junit.Assert.fail")##
$output.requireMetamodel($entity.model)##
    @Test
    @Rollback
    public void saveAndGetWithPropertySelector() {
        $entity.model.type $entity.model.var = ${entity.modelGenerator.var}.${entity.model.getter}();
#if ($entity.root.hasSimplePk() && $entity.root.primaryKey.attribute.isDate())
        ${entity.model.var}.${entity.root.primaryKey.attribute.setter}(new Date());
#end

        // add it to a Set before saving (force equals/hashcode)
        Set<$entity.model.type> set = newHashSet($entity.model.var, $entity.model.var);
        assertThat(set).hasSize(1);

        ${entity.repository.var}.save(${entity.model.var});
        entityManager.flush();

        // reload it from cache and check equality
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.property(newPropertySelector(${entity.model.type}_.$entity.root.primaryKey.attribute.var).selected($entity.model.var.${entity.root.primaryKey.attribute.getter}()));

        // clear cache to force reload from db
        entityManager.clear();

        SearchParameters ser = null;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            new DefaultSerializer().serialize(searchParameters, stream);
            byte[] bytes = stream.toByteArray();
            ser = (SearchParameters) new DefaultDeserializer().deserialize(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            fail();
        }
        
        // pk are equals...
        assertThat(${entity.model.var}).isEqualTo(${entity.repository.var}.findUnique(ser));
    }
#end


#if ($entity.getManyToOne().isNotEmpty())
    #foreach( $relation in $entity.getManyToOne().getList() )
        #if (!$relation.isMandatory())
            #set( $toOneRelation = $relation)
        #end
    #end
#end

#if ($toOneRelation)
$output.require("com.jaxio.jpa.querybyexample.SearchParameters")##
$output.requireMetamodel($entity.model)##
$output.require("java.util.List")##
    @Test
    @Rollback
    public void saveAndGetWithExplicitNullPropertySelector() {
        $entity.model.type $entity.model.var = ${entity.modelGenerator.var}.${entity.model.getter}();
#if ($entity.root.hasSimplePk() && $entity.root.primaryKey.attribute.isDate())
        ${entity.model.var}.${entity.root.primaryKey.attribute.setter}(new Date());
#end

        // add it to a Set before saving (force equals/hashcode)
        Set<$entity.model.type> set = newHashSet($entity.model.var, $entity.model.var);
        assertThat(set).hasSize(1);
        
        
        // explicitly set toOne relationship to null
        ${entity.model.var}.${toOneRelation.to.setter}(null);
        
        ${entity.repository.var}.save(${entity.model.var});
        entityManager.flush();
        
        // clear cache to force reload from db
        entityManager.clear();

        SearchParameters searchParameters = new SearchParameters() //
        .caseInsensitive() //
        .anywhere() //
        .property(${entity.model.type}_.${toOneRelation.to.var}, (Object) null);

        List<${entity.model.type}> elements = ${entity.repository.var}.find(searchParameters);
        assertThat(elements).isNotEmpty();
        for(${entity.model.type} element : elements){
            assertThat(element.${toOneRelation.to.getter}()).isNull();
        }
    }
#end
}