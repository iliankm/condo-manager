package com.ikm.condomanager.infra.aspect

import com.ikm.condomanager.domain.SelfValidating
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotEmpty
import org.springframework.stereotype.Component

@Component
class ValidationTestComponent {
    fun method1(obj1: ValidationTestDomainObject, obj2: ValidationTestDomainObject) =
        arrayOf(obj1, obj2)

    fun method2(objects: List<ValidationTestDomainObject>) =
        objects
}

class ValidationTestDomainObject(
    @field:NotEmpty
    var prop1: String,
    @field:Max(10)
    var prop2: Int
) : SelfValidating
