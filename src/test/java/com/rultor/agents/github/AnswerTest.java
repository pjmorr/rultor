/*
 * Copyright (c) 2009-2025 Yegor Bugayenko
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
package com.rultor.agents.github;

import com.jcabi.github.Comment;
import com.jcabi.github.Issue;
import com.jcabi.github.Repo;
import com.jcabi.github.mock.MkGithub;
import java.io.IOException;
import java.util.Date;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Tests for ${@link Answer}.
 *
 * @since 1.8.16
 */
final class AnswerTest {

    /**
     * Answer can post a message.
     * @throws Exception In case of error.
     */
    @Test
    void postsGithubComment() throws Exception {
        final Issue issue = AnswerTest.issue();
        issue.comments().post("hey, do it");
        new Answer(new Comment.Smart(issue.comments().get(1))).post(
            true, "hey you\u0000"
        );
        MatcherAssert.assertThat(
            "Answer with source comment should be posted",
            new Comment.Smart(issue.comments().get(2)).body(),
            Matchers.containsString("> hey, do it\n\n")
        );
    }

    /**
     * Answer can reject a message if it's a spam from us.
     * @throws Exception In case of error.
     */
    @Test
    void preventsSpam() throws Exception {
        final Issue issue = AnswerTest.issue();
        MkGithub.class.cast(issue.repo().github()).relogin("walter")
            .repos().get(issue.repo().coordinates())
            .issues().get(1).comments().post("hello, how are you?");
        final Comment.Smart comment = new Comment.Smart(
            issue.comments().get(1)
        );
        final Answer answer = new Answer(comment);
        for (int idx = 0; idx < 10; ++idx) {
            answer.post(true, "oops");
        }
        MatcherAssert.assertThat(
            "Only 5 answers should be posted",
            new ListOf<>(issue.comments().iterate(new Date(0L))).size(),
            Matchers.is(6)
        );
    }

    /**
     * Make an issue.
     * @return Issue
     * @throws IOException If fails
     */
    private static Issue issue() throws IOException {
        final Repo repo = new MkGithub().randomRepo();
        return repo.issues().create("", "");
    }

}
