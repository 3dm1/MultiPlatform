package menegatti

import kotlin.js.Date

actual fun now() = Date.now().toLong()
