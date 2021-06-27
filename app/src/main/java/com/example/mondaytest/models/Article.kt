package com.example.mondaytest.models
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class Article  @JvmOverloads constructor(
    /**
     * @return the title
     */
    /**
     * @param title the title to set
     */
    @field:Element(name = "title")
    @param:Element(name = "title")
    var title: String? = null,

    /**
     * @return the link
     */
    /**
     * @param link the link to set
     */
    @field:Element(name = "link")
    @param:Element(name = "link")
    var link: String? = null,

    /**
     * @return the description
     */
    /**
     * @param description the description to set
     */
    @field:Element(name = "description")
    @param:Element(name = "description")
    var description: String? = null

)