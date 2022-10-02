package com.dwursteisen.gltf.parser.sprite

import com.dwursteisen.gltf.parser.sprite.internal.AsepriteDataModel
import com.dwursteisen.gltf.parser.sprite.internal.FrameDetail
import com.dwursteisen.gltf.parser.sprite.internal.FrameName
import com.dwursteisen.minigdx.scene.api.Scene
import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.material.Material
import com.dwursteisen.minigdx.scene.api.model.UV
import com.dwursteisen.minigdx.scene.api.sprite.AnimationName
import com.dwursteisen.minigdx.scene.api.sprite.Frame
import com.dwursteisen.minigdx.scene.api.sprite.Sprite
import com.dwursteisen.minigdx.scene.api.sprite.SpriteAnimation
import de.matthiasmann.twl.utils.PNGDecoder
import java.io.File
import java.nio.Buffer
import java.nio.ByteBuffer

class SpriteParser(private val assetsFile: File, private val assets: AsepriteDataModel) {

    private val indexedFrames: Array<FrameDetail> = indexFrames()

    fun parse(): Scene {
        val materialId = Id()
        val spriteId = Id()
        val (decoder, data) = readImageData(assets.meta.image)
        return Scene(
            generatorVersion = SpriteParser::class.java.`package`.specificationVersion ?: "Unknown Version",
            materials = mapOf(
                materialId to Material(
                    id = materialId,
                    name = assets.meta.image,
                    width = assets.meta.size.w,
                    height = assets.meta.size.h,
                    data = data,
                    hasAlpha = decoder.hasAlpha()
                )
            ),
            sprites = mapOf(
                spriteId to Sprite(
                    id = spriteId,
                    materialReference = materialId,
                    animations = parseAnimations(),
                    uvs = indexedFrames.flatMap { frame ->
                        val w = 1 / assets.meta.size.w.toFloat()
                        val h = 1 / assets.meta.size.h.toFloat()

                        listOf(
                            UV(x = frame.frame.x * w, y = 1f - frame.frame.y * h),
                            UV(x = frame.frame.x * w, y = 1f - (frame.frame.y + frame.frame.h) * h),
                            UV(x = (frame.frame.x + frame.frame.w) * w, y = 1f - (frame.frame.y + frame.frame.h) * h),
                            UV(x = (frame.frame.x + frame.frame.w) * w, y = 1f - (frame.frame.y) * h)
                        )
                    }
                )
            )
        )
    }

    private fun readImageData(image: String): Pair<PNGDecoder, ByteArray> {
        val imagePath = if (assetsFile.isDirectory) {
            assetsFile.resolve(image)
        } else {
            assetsFile.parentFile.resolve(image)
        }
        val decoder = PNGDecoder(imagePath.inputStream())
        // create a byte buffer big enough to store RGBA values
        val txt = ByteBuffer.allocateDirect(4 * decoder.width * decoder.height)

        // decode
        decoder.decode(txt, decoder.width * 4, PNGDecoder.Format.RGBA)

        // flip the buffer so its ready to read
        (txt as Buffer).flip()

        val result = ByteArray(txt.remaining())
        txt.get(result)

        return decoder to result
    }

    private fun parseAnimations(): Map<AnimationName, SpriteAnimation> {
        return assets.meta.frameTags.map { frameTag ->
            frameTag.name to toSpriteAnimation(frameTag.name, frameTag.from, frameTag.to)
        }.ifEmpty { listOf("default" to toSpriteAnimation("default", 0, indexedFrames.size - 1)) }
            .toMap()
    }

    private fun extractIndex(frameName: FrameName): Int {
        val result = REGEX.matcher(frameName)
        result.find()
        return result.group(2).toInt()
    }

    private fun indexFrames(): Array<FrameDetail> {
        return assets.frames.map { extractIndex(it.key) to it.value }
            .sortedBy { it.first }
            .map { it.second }
            .toTypedArray()
    }

    private fun toSpriteAnimation(name: String, from: Int, to: Int): SpriteAnimation {
        val frames = indexedFrames.copyOfRange(from, to + 1)
        return SpriteAnimation(
            name = name,
            duration = frames.sumOf { it.duration } / 1000f,
            frames = frames.mapIndexed { index, it -> toFrame(from + index, it) }
        )
    }

    private fun toFrame(index: Int, frame: FrameDetail): Frame {
        return Frame(
            duration = frame.duration / 1000f,
            uvIndex = index * 4
        )
    }

    companion object {
        private val REGEX = "(.*) ([0-9]*).aseprite".toPattern()
    }
}
