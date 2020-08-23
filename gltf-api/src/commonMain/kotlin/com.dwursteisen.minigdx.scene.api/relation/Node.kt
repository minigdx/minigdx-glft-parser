package com.dwursteisen.minigdx.scene.api.relation

import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId


@Serializable
data class Node(
    @ProtoId(0)
    val reference: Id = -1,
    @ProtoId(1)
    val type: ObjectType,
    @ProtoId(2)
    val transformation: Transformation,
    @ProtoId(3)
    val children: List<Node> = emptyList(),
    @ProtoId(4)
    val customProperties: Map<String, String> = emptyMap()
)
