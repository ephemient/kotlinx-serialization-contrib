package com.github.ephemient.kotlinx.serialization.contrib.jsonjava

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AndroidRunWith(val value: KClass<*>)

interface AndroidRobolectricTestRunner
