package com.example.mondaytest.models

import org.simpleframework.xml.*

@Root(name = "rss", strict = false)
data class Feed  @JvmOverloads constructor (
    /**
     * @return the channelTitle
     */
    /**
     * @param channelTitle the channelTitle to set
     */
    @field:Element(name = "title")
    @param:Element(name = "title")
    @field:Path("channel")
    @param:Path("channel")
    //@Path("channel")
    var channelTitle: String? = null,

    /**
     * @return the articleList
     */
    /**
     * @param articleList the articleList to set
     */

    @field:ElementList(name = "item", inline = true, required = false)
    @param:ElementList(name = "item", inline = true, required = false)
    @field:Path("channel")
    @param:Path("channel")
    // @Path("channel")
    var articleList: List<Article>? =
        null
)