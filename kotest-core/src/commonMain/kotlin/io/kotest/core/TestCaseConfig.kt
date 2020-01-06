package io.kotest.core

import io.kotest.extensions.TestCaseExtension
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@UseExperimental(ExperimentalTime::class)
data class TestCaseConfig constructor(
   val enabled: Boolean = true,
   @Deprecated("to be replaced with functions")
   val invocations: Int = 1,
  // max time of the test, in millis
   @Deprecated("to be replaced with functions")
   val timeout: Duration? = null,
    // provides for concurrent execution of the test case
    // only has an effect if invocations > 1
   @Deprecated("to be replaced with functions")
   val threads: Int = 1,
   val tags: Set<Tag> = emptySet(),
   val extensions: List<TestCaseExtension> = emptyList(),
    // an issue number, or link to the issue, can be used by plugins
   val issue: String? = null) {
  init {
    require(threads > 0) { "Threads must be > 0" }
    require(invocations > 0) { "Invocations must be > 0" }
  }
}

/**
 * Creates a [TestCaseConfig] from the given parameters, reverting to the
 * receiver for null parameters.
 */
fun TestCaseConfig.deriveTestConfig(
   enabled: Boolean? = null,
   tags: Set<Tag>? = null,
   extensions: List<TestCaseExtension>? = null
) = TestCaseConfig(
   enabled ?: this.enabled,
   tags = tags ?: this.tags,
   extensions = extensions ?: this.extensions
)

/**
 * Returns the timeout for a [TestCase] taking into account global settings.
 */
@UseExperimental(ExperimentalTime::class)
expect fun TestCaseConfig.resolvedTimeout(): Duration
