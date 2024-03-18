/*
 * Copyright (c) 2009-2024 Yegor Bugayenko
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the rultor.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.rultor.agents;

import com.jcabi.github.Repos;
import com.jcabi.github.mock.MkGithub;
import com.jcabi.matchers.XhtmlMatchers;
import com.rultor.spi.Agent;
import com.rultor.spi.Profile;
import com.rultor.spi.Talk;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.xembly.Directives;

/**
 * Tests for {@link Publishes}.
 *
 * @since 1.32.7
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
final class PublishesTest {

    /**
     * Publishes can add a public attribute.
     * @throws Exception In case of error.
     */
    @Test
    void addsPublicAttribute() throws Exception {
        final MkGithub github = new MkGithub("test");
        github.repos().create(new Repos.RepoCreate("test", false));
        final Agent agent = new Publishes(new Profile.Fixed(), github);
        final Talk talk = new Talk.InFile();
        talk.modify(
            new Directives().xpath("/talk").add("archive")
                .add("log").attr("id", "abc").attr("title", "hey").up()
        );
        agent.execute(talk);
        MatcherAssert.assertThat(
            "public attribute should be added",
            talk.read(),
            XhtmlMatchers.hasXPath("/talk[@public='true']")
        );
    }

    /**
     * Publishes can ignore if PUBLIC attribute is already set.
     * @throws Exception In case of error.
     */
    @Test
    void ignoresIfPublicAttributeSet() throws Exception {
        final Agent agent = new Publishes(new Profile.Fixed(), new MkGithub());
        final Talk talk = new Talk.InFile();
        talk.modify(
            new Directives().xpath("/talk")
                .attr("public", "false")
                .add("archive")
                .add("log").attr("id", "abc").attr("title", "hey").up()
        );
        agent.execute(talk);
        MatcherAssert.assertThat(
            "public attribute should be kept false",
            talk.read(),
            XhtmlMatchers.hasXPath("/talk[@public='false']")
        );
    }

}
