/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opentable.util;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opentable.function.ThrowingConsumer;

/**
 * Transformations for various functional interfaces to modify exceptional behavior.
 */
public class ExceptionSwallower
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionSwallower.class);

    /**
     * Transform a Runnable to log and then swallow any thrown exceptions.
     */
    public static Runnable swallowExceptions(Runnable in)
    {
        return () -> {
            try {
                in.run();
            } catch (Throwable t) {
                LOGGER.error("Uncaught exception swallowed", t);
                if (t instanceof Error) {
                    throw t;
                } else if (t instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }

    /**
     * Transform a Consumer to log and then swallow any thrown exceptions.
     */
    public static <T> Consumer<T> swallowExceptions(ThrowingConsumer<T> in)
    {
        return (item) -> {
            try {
                in.accept(item);
            } catch (Throwable t) {
                LOGGER.error("Uncaught exception swallowed", t);
                if (t instanceof Error) {
                    throw (Error) t;
                } else if (t instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }
}
