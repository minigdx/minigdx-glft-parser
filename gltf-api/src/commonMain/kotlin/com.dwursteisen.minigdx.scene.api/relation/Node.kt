package com.dwursteisen.minigdx.scene.api.relation

import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId


@Serializable
data class Node(
    @ProtoId(0)
    val reference: Id,
    @ProtoId(1)
    val name: String,
    @ProtoId(2)
    val type: ObjectType,
    @ProtoId(3)
    val transformation: Transformation,
    @ProtoId(4)
    val children: List<Node> = emptyList(),
    @ProtoId(5)
    val customProperties: Map<String, String> = emptyMap()
)
