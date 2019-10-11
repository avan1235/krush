package pl.touk.exposed.generator.model

import pl.touk.exposed.generator.env.AnnotationEnvironment
import pl.touk.exposed.generator.env.TypeEnvironment
import pl.touk.exposed.generator.env.toVariableElement
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements
import javax.persistence.Column

interface EntityGraphSampleData {

    fun customerTestEntity(typeEnvironment: TypeEnvironment): TypeElement {
        return getTypeElement("pl.touk.example.Customer", typeEnvironment.elementUtils)
    }

    fun defaultPropertyNameEntity(typeEnvironment: TypeEnvironment): TypeElement {
        return getTypeElement("pl.touk.example.DefaultPropertyNameEntity", typeEnvironment.elementUtils)
    }

    fun customPropertyNameEntity(typeEnvironment: TypeEnvironment): TypeElement {
        return getTypeElement("pl.touk.example.CustomPropertyNameEntity", typeEnvironment.elementUtils)
    }

    fun nullablePropertyEntity(typeEnvironment: TypeEnvironment): TypeElement {
        return getTypeElement("pl.touk.example.NullablePropertyEntity", typeEnvironment.elementUtils)
    }

    fun customerGraphBuilder(typeEnvironment: TypeEnvironment): EntityGraphBuilder {
        val entity = customerTestEntity(typeEnvironment)

        val annEnv = AnnotationEnvironment(listOf(entity), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList())

        return EntityGraphBuilder(typeEnvironment, annEnv)
    }

    fun customerTestEntityDefinition(typeEnvironment: TypeEnvironment): EntityDefinition {
        val entity = customerTestEntity(typeEnvironment)

        return EntityDefinition(
                name = entity.simpleName, qualifiedName = entity.qualifiedName,
                table = "customers", id = null)
    }

    fun validTableMappingGraphBuilder(typeEnvironment: TypeEnvironment): EntityGraphBuilder {
        val elements = typeEnvironment.elementUtils

        val defaultPropertyNameEntity = defaultPropertyNameEntity(typeEnvironment)
        val defaultPropertyNameEntityId = getVariableElement(defaultPropertyNameEntity, elements, "id")
        val defaultPropertyNameEntityProp1 = getVariableElement(defaultPropertyNameEntity, elements, "prop1")
        val defaultPropertyNameEntityProp2 = getVariableElement(defaultPropertyNameEntity, elements,"prop2")

        val customPropertyNameEntity = customPropertyNameEntity(typeEnvironment)
        val customPropertyNameEntityId = getVariableElement(customPropertyNameEntity, elements, "id")
        val customPropertyNameEntityProp1 = getVariableElement(customPropertyNameEntity, elements, "prop1")

        val annEnv = AnnotationEnvironment(listOf(defaultPropertyNameEntity, customPropertyNameEntity),
                listOf(customPropertyNameEntityId, defaultPropertyNameEntityId),
                listOf(customPropertyNameEntityId, defaultPropertyNameEntityId),
                listOf(defaultPropertyNameEntityProp1, defaultPropertyNameEntityProp2, customPropertyNameEntityProp1),
                emptyList(), emptyList(), emptyList())
        return EntityGraphBuilder(typeEnvironment, annEnv)
    }

    fun defaultPropertyNameEntityDefinition(typeEnvironment: TypeEnvironment): EntityDefinition {
        val elements = typeEnvironment.elementUtils

        val entity = defaultPropertyNameEntity(typeEnvironment)
        val id = getVariableElement(entity, elements, "id")
        val prop1 = getVariableElement(entity, elements,"prop1")
        val prop2 = getVariableElement(entity, elements,"prop2")

        return EntityDefinition(
                name = entity.simpleName,
                qualifiedName = entity.qualifiedName,
                table = entity.simpleName.asVariable(),
                id = IdDefinition(
                        name = id.simpleName,
                        columnName = typeEnvironment.elementUtils.getName("id"),
                        annotation = id.getAnnotation(Column::class.java),
                        type = IdType.LONG,
                        typeMirror = id.asType(),
                        generatedValue = true
                ),
                properties = listOf(
                        PropertyDefinition(
                                name = typeEnvironment.elementUtils.getName("prop1"),
                                columnName = typeEnvironment.elementUtils.getName("prop1"),
                                annotation = prop1.getAnnotation(Column::class.java),
                                type = PropertyType.STRING,
                                typeMirror = id.asType(),
                                nullable = false
                        ),
                        PropertyDefinition(
                                name = typeEnvironment.elementUtils.getName("prop2"),
                                columnName = typeEnvironment.elementUtils.getName("prop2"),
                                annotation = prop2.getAnnotation(Column::class.java),
                                type = PropertyType.STRING,
                                typeMirror = id.asType(),
                                nullable = false
                        )
                )
        )
    }

    fun customPropertyNameEntityDefinition(typeEnvironment: TypeEnvironment): EntityDefinition {
        val elements = typeEnvironment.elementUtils

        val entity = customPropertyNameEntity(typeEnvironment)
        val id = getVariableElement(entity, elements, "id")
        val prop1 = getVariableElement(entity, elements,"prop1")

        return EntityDefinition(
                name = entity.simpleName,
                qualifiedName = entity.qualifiedName,
                table = "entity",
                id = IdDefinition(
                        name = id.simpleName,
                        columnName = typeEnvironment.elementUtils.getName("test_id"),
                        annotation = id.getAnnotation(Column::class.java),
                        type = IdType.LONG,
                        typeMirror = id.asType(),
                        generatedValue = true
                ),
                properties = listOf(
                        PropertyDefinition(
                                name = typeEnvironment.elementUtils.getName("prop1"),
                                columnName = typeEnvironment.elementUtils.getName("prop1_custom"),
                                annotation = prop1.getAnnotation(Column::class.java),
                                type = PropertyType.STRING,
                                typeMirror = id.asType(),
                                nullable = false
                        )
                )
        )
    }

    fun nullablePropertyGraphBuilder(typeEnvironment: TypeEnvironment): EntityGraphBuilder {
        val entity = nullablePropertyEntity(typeEnvironment)
        val id = getVariableElement(entity, typeEnvironment.elementUtils,"id")
        val prop1 = getVariableElement(entity, typeEnvironment.elementUtils,"prop1")

        val annEnv = AnnotationEnvironment(listOf(entity), listOf(id), listOf(id), listOf(prop1),
                emptyList(), emptyList(), emptyList())

        return EntityGraphBuilder(typeEnvironment, annEnv)
    }

    fun nullablePropertyEntityDefinition(typeEnvironment: TypeEnvironment): EntityDefinition {
        val entity = nullablePropertyEntity(typeEnvironment)
        val id = getVariableElement(entity, typeEnvironment.elementUtils,"id")
        val prop1 = getVariableElement(entity, typeEnvironment.elementUtils,"prop1")

        return EntityDefinition(
                name = entity.simpleName, qualifiedName = entity.qualifiedName,
                table = "nullablePropertyEntity",
                id = IdDefinition(
                        name = id.simpleName,
                        columnName = typeEnvironment.elementUtils.getName("id"),
                        annotation = id.getAnnotation(Column::class.java),
                        type = IdType.LONG,
                        typeMirror = id.asType(),
                        generatedValue = true
                ),
                properties = listOf(
                        PropertyDefinition(
                                name = typeEnvironment.elementUtils.getName("prop1"),
                                columnName = typeEnvironment.elementUtils.getName("prop1"),
                                annotation = prop1.getAnnotation(Column::class.java),
                                type = PropertyType.STRING,
                                typeMirror = id.asType(),
                                nullable = true
                        )

                ))
    }

    private fun getTypeElement(name: String, elements: Elements): TypeElement = elements.getTypeElement(name)

    private fun getVariableElement(typeElt: TypeElement, elements: Elements, name: String): VariableElement =
            elements.getAllMembers(typeElt)
                    .filter { it.simpleName.contentEquals(name) }
                    .map(Element::toVariableElement)
                    .first()
}