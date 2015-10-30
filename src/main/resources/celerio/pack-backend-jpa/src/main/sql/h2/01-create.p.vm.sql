$output.sql("h2/generated", "01-create.sql")##

DROP ALL objects;
#foreach($table in $metadata.tables)


-- =================================
-- TABLE $table.name 
-- =================================

CREATE TABLE $table.name (
#foreach($column in $table.columns)
	${column.h2}$project.print($velocityHasNext, ",", ");")
#end
#foreach($indexName in $table.indexHoldersByName.keySet()) 
#if (!$indexName.startsWith("PRIMARY_KEY_"))
#set($indexHolder = $table.getIndexHolderByName($indexName))##

CREATE#if ($indexHolder.isUnique()) UNIQUE#end INDEX IF NOT EXISTS $indexHolder.name ON $table.name (#foreach($index in $indexHolder.indexes)${index.columnName}$project.print($velocityHasNext, ", ", ");")#end
#end
#end
#if($table.hasH2IdentityPk())

ALTER TABLE $table.name ALTER COLUMN $table.primaryKey IDENTITY;
#elseif($table.hasPk())

CREATE PRIMARY KEY ON $table.name (#foreach($pkName in $table.primaryKeys)${pkName}$project.print($velocityHasNext, ", ", ");")#end
#end
#end
#foreach($table in $metadata.tables)
#foreach($foreignKeyName in $table.getForeignKeysByName().keySet())
#set($foreignKey = $table.getForeignKeyByName($foreignKeyName))##

ALTER TABLE $table.name ADD CONSTRAINT $foreignKeyName FOREIGN KEY (#foreach($importedKey in $foreignKey.importedKeys)${importedKey.fkColumnName}$project.print($velocityHasNext, ",", ")")#end REFERENCES $foreignKey.pkTableName (#foreach($importedKey in $foreignKey.importedKeys)${importedKey.pkColumnName}$project.print($velocityHasNext, ",", ");")#end
#end
#end
