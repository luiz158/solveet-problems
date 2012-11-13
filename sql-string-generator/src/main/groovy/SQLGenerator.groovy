/**
 * Complete solution and tests at: https://github.com/rchavarria/solveet-problems/tree/master/sql-string-generator
 */
class SQLGenerator {

    def table
    def columns
    def criteria
    def updateValues

    def table(table) {
        this.table = table

        this
    }

    def columns(columns) {
        this.columns = columns

        this
    }

    def criteria(criteria) {
        this.criteria = criteria

        this
    }

    def newValues(updateValues) {
        this.updateValues = updateValues

        this
    }

    def select() {
        def columns = generateColumns()

        def sql = "SELECT ${columns} FROM ${table}"
        sql += generateCriteria()
        sql.toString()
    }

    private generateColumns() {
        if(columns == null) "*"
        else columns.join(", ")[0..-1]
    }

    private generateCriteria() {
        if(criteria == null) return ""

        def clauses = criteria.keySet().collect { it + "=" + translateCriteriaValue(criteria[it]) }
        def sql = clauses.sort().inject(" WHERE ") {sql, item ->
            sql + item + " AND "
        }

        sql[0..-6]
    }

    private translateCriteriaValue(value) {
        if(value instanceof Number) value
        else if (value instanceof Boolean) "'${value.toString().toUpperCase()}'"
        else "'${value}'"
    }

    def delete() {
        def sql = "DELETE FROM ${table}"
        sql += generateCriteria()
        sql.toString()
    }

    def insert() {
        def sql = "INSERT INTO ${table}"
        sql += generateInsertCriteria()
        sql.toString()
    }

    private generateInsertCriteria() {
        def sql = " (" + criteria.keySet().sort().toString()[1..-2] + ")"
        def orderedValues = criteria.keySet().sort().inject("") { str, k -> 
            str + translateCriteriaValue(criteria[k]) + ", " 
        }
        sql += " VALUES (" + orderedValues[0..-3] + ")"

        sql.toString()
    }

    def update() {
        def sql = "UPDATE ${table}"
        sql += generateUpdateValues()
        sql += generateCriteria()
        sql.toString()
    }

    private generateUpdateValues() {
        def valuesList = updateValues.keySet().collect { k ->
            k + "=" + translateCriteriaValue(updateValues[k])
        }
        
        def sql = valuesList.sort().inject(" SET ") {sql, item ->
            sql + item + ", "
        }

        sql[0..-3]
    }
}

// Showing how to use the class
/*
*/
