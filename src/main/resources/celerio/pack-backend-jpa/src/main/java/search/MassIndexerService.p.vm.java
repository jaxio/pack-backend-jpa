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
$output.skipIf($project.search.isEmpty())##
$output.java($Search, "MassIndexerService")##

$output.requireStatic("org.hibernate.search.jpa.Search.getFullTextEntityManager")##
$output.require("java.util.Arrays")##
$output.require("javax.annotation.PostConstruct")##
$output.require("javax.persistence.EntityManager")##
$output.require("javax.persistence.PersistenceContext")##
$output.require("org.apache.commons.lang.time.StopWatch")##
$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.beans.factory.annotation.Value")##
#foreach($entity in $project.search.list)
$output.require($entity.model)##
#end

$output.dynamicAnnotationTakeOver("javax.inject.Named")##
#if (!$output.isAbstract())
$output.require("org.springframework.context.annotation.Lazy")##
@Lazy(false)
#end
public class $output.currentClass {
    private static final Logger log = LoggerFactory.getLogger(${output.currentClass}.class);
    protected static final Class<?>[] CLASSES_TO_BE_INDEXED = { //
#foreach($entity in $project.search.list)
        ${entity.model.type}.class #if($velocityHasNext), // 
                                   #end
#end
    };
    @PersistenceContext
    protected EntityManager entityManager;
    @Value("${dollar}{massIndexer.nbThreadsToLoadObjects:1}")
    protected int threadsToLoadObjects;
    @Value("${dollar}{massIndexer.batchSizeToLoadObjects:10}")
    protected int batchSizeToLoadObjects;
    @Value("${dollar}{massIndexer.nbThreadsForSubsequentFetching:1}")
    protected int threadsForSubsequentFetching;

    @PostConstruct
    public void createIndex() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
			for (Class<?> classToBeIndexed : CLASSES_TO_BE_INDEXED) {
				indexClass(classToBeIndexed);
			}
		} finally {
			stopWatch.stop();
			log.info("Indexed {} in {}", Arrays.toString(CLASSES_TO_BE_INDEXED), stopWatch.toString());
		}
	}

	private void indexClass(Class<?> classToBeIndexed) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
            getFullTextEntityManager(entityManager) //
					.createIndexer(classToBeIndexed) //
                    .batchSizeToLoadObjects(batchSizeToLoadObjects) //
                    .threadsToLoadObjects(threadsToLoadObjects) //
                    .threadsForSubsequentFetching(threadsForSubsequentFetching) //
					.startAndWait();
		} catch (InterruptedException e) {
			log.warn("Interrupted while indexing " + classToBeIndexed.getSimpleName(), e);
			Thread.currentThread().interrupt();
        } finally {
            stopWatch.stop();
			log.info("Indexed {} in {}", classToBeIndexed.getSimpleName(), stopWatch.toString());
        }
    }
}