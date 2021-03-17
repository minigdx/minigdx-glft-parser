package com.dwursteisen.minigdx.scene.api

import com.dwursteisen.minigdx.scene.api.armature.Animation
import com.dwursteisen.minigdx.scene.api.armature.Armature
import com.dwursteisen.minigdx.scene.api.camera.Camera
import com.dwursteisen.minigdx.scene.api.camera.OrthographicCamera
import com.dwursteisen.minigdx.scene.api.camera.PerspectiveCamera
import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.light.PointLight
import com.dwursteisen.minigdx.scene.api.material.Material
import com.dwursteisen.minigdx.scene.api.model.Box
import com.dwursteisen.minigdx.scene.api.model.Model
import com.dwursteisen.minigdx.scene.api.relation.Node
import com.dwursteisen.minigdx.scene.api.sprite.Sprite
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
data class Scene(
    @ProtoNumber(1)
    val perspectiveCameras: Map<Id, Camera> = emptyMap(),
    @ProtoNumber(2)
    val orthographicCameras: Map<Id, Camera> = emptyMap(),
    @ProtoNumber(3)
    val models: Map<Id, Model> = emptyMap(),
    @ProtoNumber(4)
    val materials: Map<Id, Material> = emptyMap(),
    @ProtoNumber(5)
    val pointLights: Map<Id, PointLight> = emptyMap(),
    @ProtoNumber(6)
    val armatures: Map<Id, Armature> = emptyMap(),
    @ProtoNumber(7)
    val animations: Map<Id, List<Animation>> = emptyMap(),
    @ProtoNumber(8)
    val boxes: Map<Id, Box> = emptyMap(),
    @ProtoNumber(9)
    val children: List<Node> = emptyList(),
    @ProtoNumber(10)
    val sprites: Map<Id, Sprite> = emptyMap()
) {

    companion object {

        private val serialModule = SerializersModule {
            polymorphic(Camera::class) {
                subclass(PerspectiveCamera::class)
                subclass(OrthographicCamera::class)
            }
        }

        @ExperimentalStdlibApi
        fun readJson(data: ByteArray): Scene {
            val json = Json { serializersModule = serialModule }
            return json.decodeFromString(data.decodeToString())
        }

        @ExperimentalSerializationApi
        fun readProtobuf(data: ByteArray): Scene {
            val protoBuf = ProtoBuf { serializersModule = serialModule }
            return protoBuf.decodeFromByteArray(data)
        }

        @ExperimentalStdlibApi
        fun writeJson(model: Scene): ByteArray {
            val json = Json { serializersModule = serialModule }
            return json.encodeToString(model).encodeToByteArray()
        }

        @ExperimentalStdlibApi
        fun writeProtobuf(model: Scene): ByteArray {
            val protoBuf = ProtoBuf { serializersModule = serialModule }
            return protoBuf.encodeToByteArray(model)
        }
    }
}
