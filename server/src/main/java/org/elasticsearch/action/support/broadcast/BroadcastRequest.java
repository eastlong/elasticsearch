/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.support.broadcast;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.IndicesRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.unit.TimeValue;

import java.io.IOException;

public class BroadcastRequest<Request extends BroadcastRequest<Request>> extends ActionRequest implements IndicesRequest.Replaceable {

    protected String[] indices;
    private IndicesOptions indicesOptions;

    @Nullable // if timeout is infinite
    private TimeValue timeout;

    public BroadcastRequest(StreamInput in) throws IOException {
        super(in);
        indices = in.readStringArray();
        indicesOptions = IndicesOptions.readIndicesOptions(in);
        timeout = in.readOptionalTimeValue();
    }

    protected BroadcastRequest(String... indices) {
        this(indices, IndicesOptions.strictExpandOpenAndForbidClosed());
    }

    protected BroadcastRequest(String[] indices, IndicesOptions indicesOptions) {
        this(indices, indicesOptions, null);
    }

    protected BroadcastRequest(String[] indices, IndicesOptions indicesOptions, @Nullable TimeValue timeout) {
        this.indices = indices;
        this.indicesOptions = indicesOptions;
        this.timeout = timeout;
    }

    @Override
    public String[] indices() {
        return indices;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Request indices(String... indices) {
        this.indices = indices;
        return (Request) this;
    }

    @Override
    public ActionRequestValidationException validate() {
        return null;
    }

    @Override
    public IndicesOptions indicesOptions() {
        return indicesOptions;
    }

    @SuppressWarnings("unchecked")
    public final Request indicesOptions(IndicesOptions indicesOptions) {
        this.indicesOptions = indicesOptions;
        return (Request) this;
    }

    @Nullable // if timeout is infinite
    public TimeValue timeout() {
        return timeout;
    }

    @SuppressWarnings("unchecked")
    public final Request timeout(@Nullable TimeValue timeout) {
        this.timeout = timeout;
        return (Request) this;
    }

    @Override
    public boolean includeDataStreams() {
        return true;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeStringArrayNullable(indices);
        indicesOptions.writeIndicesOptions(out);
        out.writeOptionalTimeValue(timeout);
    }
}
