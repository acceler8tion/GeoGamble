package com.acceler8tion.geogamble.api.service.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Preview(val preview: String) {
}