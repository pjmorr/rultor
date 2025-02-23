/*
 * SPDX-FileCopyrightText: Copyright (c) 2009-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.rultor.agents.github.qtn;

import com.jcabi.github.Comment;
import com.jcabi.github.Issue;
import com.jcabi.github.Repo;
import com.jcabi.github.mock.MkGithub;
import com.jcabi.matchers.XhtmlMatchers;
import java.net.URI;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.xembly.Directives;
import org.xembly.Xembler;

/**
 * Tests for ${@link QnDeploy}.
 *
 * @since 1.6
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
final class QnDeployTest {

    /**
     * QnDeploy can build a request.
     * @throws Exception In case of error.
     */
    @Test
    void buildsRequest() throws Exception {
        final Repo repo = new MkGithub().randomRepo();
        final Issue issue = repo.issues().create("", "");
        issue.comments().post("deploy");
        MatcherAssert.assertThat(
            "Deploy request should be created",
            new Xembler(
                new Directives().add("request").append(
                    new QnDeploy().understand(
                        new Comment.Smart(issue.comments().get(1)), new URI("#")
                    ).dirs()
                )
            ).xml(),
            XhtmlMatchers.hasXPaths(
                "/request[type='deploy']",
                "/request/args[count(arg) = 2]",
                "/request/args/arg[@name='head']",
                "/request/args/arg[@name='head_branch']"
            )
        );
    }

}
