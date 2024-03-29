/**
 * Copyright (C) 2011 rwoo@gmx.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.streamflyer.regex.addons.stateful;

import java.util.List;

import com.googlecode.streamflyer.regex.MatchProcessor;
import com.googlecode.streamflyer.regex.ReplacingProcessor;
import com.googlecode.streamflyer.regex.addons.tokens.Token;
import com.googlecode.streamflyer.regex.addons.util.DoNothingProcessor;

/**
 * A state of the match process. The state is reached if the corresponding {@link #getToken() token} is matched.
 * <p>
 * The state transitions can be set via {@link #setTransitions(List, TransitionGuard)}.
 * 
 * @author rwoo
 * @since 1.1.0
 */
public class State {

    /**
     * The token that must be matched to reach this state.
     */
    private Token token;

    /**
     * The transitions that can be applied.
     */
    private Transitions transitions;

    /**
     * A constructor to create an initial state.
     * 
     * @param stateName
     *            A unique name for the state.
     */
    public State(String stateName) {
        this(stateName, "", new DoNothingProcessor());
    }

    /**
     * If this constructed state is reached, the stream is {@link DoNothingProcessor not modified}.
     * 
     * @param stateName
     *            A unique name for the state.
     * @param regex
     *            In order to reach this state, this regular expression must be matched.
     */
    public State(String stateName, String regex) {
        this(stateName, regex, new DoNothingProcessor());
    }

    /**
     * This constructor defines a state so that text of the matched state token is replaced with the given replacement.
     * 
     * @param stateName
     *            A unique name for the state.
     * @param regex
     *            In order to reach this state, this regular expression must be matched.
     * @param replacement
     *            The replacement defines how the text that is matched via <code>regex</code> shall be replaced.
     */
    public State(String stateName, String regex, String replacement) {
        this(stateName, regex, new ReplacingProcessor(replacement));
    }

    /**
     * If the constructed state is reached then the given {@link MatchProcessor} modifies the stream.
     * 
     * @param stateName
     *            A unique name for the state.
     * @param regex
     *            In order to reach this state, this regular expression must be matched.
     * @param matchProcessor
     *            If the constructed state is reached then the given {@link MatchProcessor} modifies the stream.
     */
    public State(String stateName, String regex, MatchProcessor matchProcessor) {
        this(new Token(stateName, regex, matchProcessor));
    }

    /**
     * The constructed state is reached if the given token is matched.
     * 
     * @param token
     *            See {@link #token}
     */
    public State(Token token) {
        super();
        this.token = token;
    }

    /**
     * Convenience method.
     * <p>
     * Sets {@link #transitions} as defined by the reachable states and the transition guard.
     * 
     * @param endStates
     *            the states that can be reached from the this state.
     * @param transitionGuard
     *            this guard can stop the transition or add additional logic to the transition.
     */
    public void setTransitions(List<State> endStates, TransitionGuard transitionGuard) {
        setTransitions(new Transitions(endStates, transitionGuard));
    }

    /**
     * Sets {@link #transitions}.
     * 
     * @param transitions
     *            the transitions to set.
     */
    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }

    /**
     * @return Returns the token that must be matched to reach this state.
     */
    public Token getToken() {
        return token;
    }

    /**
     * @return Returns the token processor that must be used to switch to the next state.
     */
    public Transitions getTransitions() {
        return transitions;
    }

    @Override
    public String toString() {
        return "State [token=" + token + ", transitions=" + transitions + "]";
    }

}
