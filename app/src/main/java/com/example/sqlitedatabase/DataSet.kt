package com.example.sqlitedatabase

data class DataSet(
    val studentID:Int,
    val studentName:String,
    val studentAddress:String,
    val studentClass:String,
    val age: Int,
    val img:ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataSet

        if (studentID != other.studentID) return false
        if (studentName != other.studentName) return false
        if (studentAddress != other.studentAddress) return false
        if (studentClass != other.studentClass) return false
        if (age != other.age) return false
        if (!img.contentEquals(other.img)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = studentID
        result = 31 * result + studentName.hashCode()
        result = 31 * result + studentAddress.hashCode()
        result = 31 * result + studentClass.hashCode()
        result = 31 * result + age
        result = 31 * result + img.contentHashCode()
        return result
    }
}
