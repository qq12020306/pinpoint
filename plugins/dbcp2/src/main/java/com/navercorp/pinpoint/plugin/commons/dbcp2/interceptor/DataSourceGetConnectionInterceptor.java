/*
 * Copyright 2016 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.plugin.commons.dbcp2.interceptor;

import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.SpanEventRecorder;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.interceptor.SpanEventSimpleAroundInterceptorForPlugin;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.Scope;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.TargetMethod;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.TargetMethods;
import com.navercorp.pinpoint.plugin.commons.dbcp2.CommonsDbcp2Constants;

@Scope(CommonsDbcp2Constants.SCOPE)
@TargetMethods({
        @TargetMethod(name="getConnection"),
        @TargetMethod(name="getConnection", paramTypes={"java.lang.String", "java.lang.String"})
})
public class DataSourceGetConnectionInterceptor extends SpanEventSimpleAroundInterceptorForPlugin {

    public DataSourceGetConnectionInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        super(traceContext, descriptor);
    }

    @Override
    public void doInBeforeTrace(SpanEventRecorder recorder, final Object target, Object[] args) {
    }

    @Override
    public void doInAfterTrace(SpanEventRecorder recorder, Object target, Object[] args, Object result, Throwable throwable) {
        recorder.recordServiceType(CommonsDbcp2Constants.SERVICE_TYPE);
        if (args == null) {
//          getConnection() without any arguments
            recorder.recordApi(getMethodDescriptor());
        } else if(args.length == 2) {
//          skip args[1] because it's a password.
            recorder.recordApi(getMethodDescriptor(), args[0], 0);
        }
        recorder.recordException(throwable);
    }

}
