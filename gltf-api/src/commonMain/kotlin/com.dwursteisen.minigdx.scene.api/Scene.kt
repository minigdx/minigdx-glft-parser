package com.dwursteisen.minigdx.scene.api

import com.dwursteisen.minigdx.scene.api.armature.Animation
import com.dwursteisen.minigdx.scene.api.armature.Armature
import com.dwursteisen.minigdx.scene.api.camera.Camera
import com.dwursteisen.minigdx.scene.api.camera.OrthographicCamera
import com.dwursteisen.minigdx.scene.api.camera.PerspectiveCamera
import com.dwursteisen.minigdx.scene.api.light.PointLight
import com.dwursteisen.minigdx.scene.api.material.Material
import com.dwursteisen.minigdx.scene.api.model.Model
import kotlinx.serialization.Serializable
import kotlinx.serialization.dump
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class Scene(
    @ProtoNumber(0)
    val perspectiveCameras: Map<String, Camera> = emptyMap(),
    @ProtoNumber(1)
    val orthographicCameras: Map<String, Camera> = emptyMap(),
    @ProtoNumber(2)
    val models: Map<String, Model> = emptyMap(),
    @ProtoNumber(3)
    val materials: Map<String, Material> = emptyMap(),
    @ProtoNumber(4)
    val pointLights: Map<String, PointLight> = emptyMap(),
    @ProtoNumber(5)
    val armatures: Map<Int, Armature> = emptyMap(),
    @ProtoNumber(6)
    val animations: Map<Int, List<Animation>> = emptyMap()
) {

    companion object {
        @ExperimentalStdlibApi
        fun readJson(data: ByteArray): Scene {
            val deserializer = Json { serializersModule = serialModule() }
            return deserializer.decodeFromString(serializer(), data.decodeToString())
        }

        fun readProtobuf(data: ByteArray): Scene {
            val deserializer = ProtoBuf { serializersModule = serialModule() }
            return deserializer.decodeFromByteArray(serializer(), data)
        }

        private fun serialModule(): SerializersModule {
            return SerializersModule {
                polymorphic(
                    Camera::class,
                    PerspectiveCamera::class,
                    PerspectiveCamera.serializer()
                )
                polymorphic(
                    Camera::class,
                    OrthographicCamera::class,
                    OrthographicCamera.serializer()
                )
            }
        }

        @ExperimentalStdlibApi
        fun writeJson(model: Scene): ByteArray {
            val serializer = Json { serializersModule = serialModule() }
            return serializer.encodeToString(serializer(), model).encodeToByteArray()
        }

        fun writeProtobuf(model: Scene): ByteArray {
            val serializer = ProtoBuf { serializersModule = serialModule() }
            return serializer.encodeToByteArray(serializer(), model)
        }
    }
}
