package com.dwursteisen.minigdx.scene.api

import com.dwursteisen.minigdx.scene.api.camera.Camera
import com.dwursteisen.minigdx.scene.api.camera.OrthographicCamera
import com.dwursteisen.minigdx.scene.api.camera.PerspectiveCamera
import com.dwursteisen.minigdx.scene.api.material.Material
import com.dwursteisen.minigdx.scene.api.model.Model
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class Scene(
    @ProtoId(0)
    val perspectiveCameras: Map<String, Camera> = emptyMap(),
    @ProtoId(1)
    val orthographicCameras: Map<String, Camera> = emptyMap(),
    @ProtoId(2)
    val models: Map<String, Model> = emptyMap(),
    @ProtoId(3)
    val materials: Map<String, Material> = emptyMap()
) {

    companion object {
        @ExperimentalStdlibApi
        fun readJson(data: ByteArray): Scene {
            val deserializer = Json(context = serialModule())
            return deserializer.parse(serializer(), data.decodeToString())
        }

        fun readProtobuf(data: ByteArray): Scene {
            val deserializer = ProtoBuf(context = serialModule())
            return deserializer.load(serializer(), data)
        }

        private fun serialModule(): SerialModule {
            return SerializersModule {
                polymorphic<Camera> {
                    PerspectiveCamera::class with PerspectiveCamera.serializer()
                    OrthographicCamera::class with OrthographicCamera.serializer()
                }
            }
        }

        @ExperimentalStdlibApi
        fun writeJson(model: Scene): ByteArray {
            val serializer = Json(context = serialModule())
            return serializer.stringify(serializer(), model).encodeToByteArray()
        }

        fun writeProtobuf(model: Scene): ByteArray {
            val serializer = ProtoBuf(context = serialModule())
            return serializer.dump(serializer(), model)
        }
    }
}
