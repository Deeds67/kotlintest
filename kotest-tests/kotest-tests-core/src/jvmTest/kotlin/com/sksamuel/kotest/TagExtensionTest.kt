package com.sksamuel.kotest

import io.kotest.Project
import io.kotest.SpecClass
import io.kotest.core.Tag
import io.kotest.core.Tags
import io.kotest.core.TestCase
import io.kotest.core.TestResult
import io.kotest.core.TestStatus
import io.kotest.extensions.TagExtension
import io.kotest.shouldBe
import io.kotest.specs.StringSpec

class TagExtensionTest : StringSpec() {

  object TagA : Tag()
  object TagB : Tag()

  private val ext = object : TagExtension {
    override fun tags(): Tags =
        Tags(setOf(TagA), setOf(TagB))
  }

  override fun afterSpecClass(spec: SpecClass, results: Map<TestCase, TestResult>) {
    results.map { it.key.name to it.value.status }.toMap() shouldBe mapOf(
        "should be tagged with tagA and therefore included" to TestStatus.Success,
        "should be untagged and therefore excluded" to TestStatus.Ignored,
        "should be tagged with tagB and therefore excluded" to TestStatus.Ignored
    )
  }

  override fun beforeSpec(spec: SpecClass) {
    Project.registerExtension(ext)
  }

  override fun afterSpec(spec: SpecClass) {
    Project.deregisterExtension(ext)
  }

  init {
    "should be tagged with tagA and therefore included".config(tags = setOf(TagA)) { }

    "should be untagged and therefore excluded" { }

    "should be tagged with tagB and therefore excluded".config(tags = setOf(TagB)) { }
  }
}
