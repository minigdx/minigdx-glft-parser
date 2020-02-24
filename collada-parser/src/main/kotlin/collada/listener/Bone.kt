package collada.listener

class Bone(val id: String, val parent: Bone?, var childs: List<Bone> = emptyList(), val matrix: FloatArray)
