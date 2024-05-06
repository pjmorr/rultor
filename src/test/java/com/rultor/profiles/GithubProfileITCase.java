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
package com.rultor.profiles;

import com.jcabi.github.Coordinates;
import com.jcabi.github.RtGithub;
import com.jcabi.matchers.XhtmlMatchers;
import com.rultor.spi.Profile;
import com.yegor256.WeAreOnline;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Tests for {@link GithubProfile}.
 *
 * @since 1.0
 */
@SuppressWarnings("PMD.UseUtilityClass")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(WeAreOnline.class)
final class GithubProfileITCase {

    /**
     * GithubProfile can fetch a YAML config.
     * @throws Exception In case of error.
     */
    @Test
    @Disabled
    void fetchesYamlConfig() throws Exception {
        final Profile profile = new GithubProfile(
            new RtGithub().repos().get(
                new Coordinates.Simple("yegor256/rultor")
            )
        );
        MatcherAssert.assertThat(
            "script for merge should be read",
            profile.read(),
            XhtmlMatchers.hasXPaths(
                "/p/entry[@key='merge']/entry[@key='script']"
            )
        );
    }
}
