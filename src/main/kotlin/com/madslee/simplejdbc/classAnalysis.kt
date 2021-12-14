package com.madslee.simplejdbc

import javax.script.ScriptEngineManager

private val engine = ScriptEngineManager().getEngineByExtension("kts")!!

val fieldNames: (Any) -> List<String> = { instance ->
    instance.javaClass.declaredFields.map { it.name }
}

val fieldMap: (Any) -> Map<String, Any> = { instance ->
    instance.javaClass.declaredFields.map { it.name to engine.eval("any.get${it.name}()") }.toMap()
}

/*
val engine = ScriptEngineManager().getEngineByExtension("kts")!!
engine.eval("val x = 3")
val res = engine.eval("x + 2")
Assert.assertEquals(5, res)
 */