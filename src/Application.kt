package com.example

import com.fasterxml.jackson.databind.SerializationFeature
import com.google.gson.GsonBuilder
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import io.ktor.client.features.logging.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

val header1 = HeaderItem(
    "مصاحف قراءة اﻹمام نافع المدني",
    listOf(
        Mushaf(
            "kallon",
            604,
            "مصحف رواية قالون عن نافع",
            "من طريق أبي نشيط (بقصر المنفصل وصلة ميم الجمع)",
            "http://www.alwa7y.com/keraat/kaloon/",
            true,
            100,
            109.0,
            14.5
        ),
        Mushaf(
            "warsh",
            604,
            "مصحف رواية ورش عن نافع",
            "من طريق الأزرق",
            "http://www.alwa7y.com/keraat/warsh/",
            true,
            115,
            19.0,
            115.0
        )
    )
)

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        install(StatusPages){
            this.exception<Throwable>{e->
                call.respondText(e.localizedMessage, ContentType.Text.Plain)
                throw e
            }
        }
        install(ContentNegotiation) {
            jackson{
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }
        routing {
            get("/") {
                call.respond(header1)
            }
            get("/demo") {
                call.respondText("HELLO WORLD!")
            }
        }
    }
    server.start(wait = true)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        install(Logging) {
            level = LogLevel.HEADERS
        }


    }

    runBlocking {
        // Sample for making a HTTP Client request
        /*
        val message = client.post<JsonSampleClass> {
            url("http://127.0.0.1:8080/path/to/endpoint")
            contentType(ContentType.Application.Json)
            body = JsonSampleClass(hello = "world")
        }
        */
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"HTML" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                }
            }
        }

        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.red
                }
                p {
                    fontSize = 2.em
                }
                rule("p.myclass") {
                    color = Color.blue
                }
            }
        }
    }
}

data class JsonSampleClass(val hello: String)

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style(type = ContentType.Text.CSS.toString()) {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
