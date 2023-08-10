package com.ikm.condomanager.aspect

import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertNotNull

/**
 * Spring test for [DomainObjectsValidationAspect].
 */
@ExtendWith(SpringExtension::class)
@EnableAspectJAutoProxy
@ContextConfiguration(classes = [DomainObjectsValidationAspect::class, ValidationTestComponent::class])
class DomainObjectsValidationAspectTest {
    @Autowired
    lateinit var validationTestComponent: ValidationTestComponent

    @Test
    fun `should validate arguments without errors`() {
        // given
        val obj1 = ValidationTestDomainObject(prop1 = "Some text", prop2 = 1)
        val obj2 = ValidationTestDomainObject(prop1 = "Some other text", prop2 = 2)
        // when
        val result = validationTestComponent.method1(obj1, obj2)
        // then
        assertTrue(result.size == 2)
    }

    @Test
    fun `should validate Iterable argument without errors`() {
        // given
        val l = listOf(
            ValidationTestDomainObject(prop1 = "Some text", prop2 = 1),
            ValidationTestDomainObject(prop1 = "Some other text", prop2 = 2)
        )
        // when
        val result = validationTestComponent.method2(l)
        // then
        assertSame(l, result)
    }

    @Test
    fun `should validate arguments with errors`() {
        // given
        val valid = ValidationTestDomainObject(prop1 = "Some text", prop2 = 1)
        val notValid = ValidationTestDomainObject(prop1 = "Some text", prop2 = 1)
        notValid.prop1 = ""
        notValid.prop2 = 100
        // when
        val ex = assertThrows(ConstraintViolationException::class.java) {
            validationTestComponent.method1(valid, notValid)
        }
        // then
        assertNotNull(ex)
    }

    @Test
    fun `should validate Iterable argument with errors`() {
        // given
        val valid = ValidationTestDomainObject(prop1 = "Some text", prop2 = 1)
        val notValid = ValidationTestDomainObject(prop1 = "Some text", prop2 = 1)
        notValid.prop1 = ""
        notValid.prop2 = 100
        val l = listOf(valid, notValid)
        // when
        val ex = assertThrows(ConstraintViolationException::class.java) {
            validationTestComponent.method2(l)
        }
        // then
        assertNotNull(ex)
    }
}
