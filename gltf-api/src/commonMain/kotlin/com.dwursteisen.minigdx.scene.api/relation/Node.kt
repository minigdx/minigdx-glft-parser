package com.dwursteisen.minigdx.scene.api.relation

import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
data class Node(
    @ProtoNumber(1)
    val reference: Id,
    @ProtoNumber(2)
    val name: String,
    @ProtoNumber(3)
    val type: ObjectType,
    @ProtoNumber(4)
    val transformation: Transformation,
    @ProtoNumber(5)
    val children: List<Node> = emptyList(),
    @ProtoNumber(6)
    val customProperties: Map<String, String> = emptyMap()
)
