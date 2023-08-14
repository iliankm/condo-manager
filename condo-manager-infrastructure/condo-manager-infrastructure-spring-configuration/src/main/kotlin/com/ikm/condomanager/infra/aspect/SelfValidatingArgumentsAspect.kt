package com.ikm.condomanager.infra.aspect

import com.ikm.condomanager.domain.SelfValidating
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

/**
 * Aspect for validation of [SelfValidating] objects passed as arguments to the Spring components etc.
 */
@Component
@Aspect
class SelfValidatingArgumentsAspect {
    /**
     * Advice for domain objects validation when passed to the components.
     */
    @Before("execution(* com.ikm.condomanager..*.*(..))")
    fun doValidationBeforeMethodsExecution(jp: JoinPoint) =
        jp.args.forEach { arg ->
            when (arg) {
                is SelfValidating<*> -> arg.validate()
                is Iterable<*> -> arg.validateAll()
            }
        }
}

private fun Iterable<*>.validateAll() =
    forEach {
        if (it is SelfValidating<*>) it.validate()
    }
