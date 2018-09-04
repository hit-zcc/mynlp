/*
 * Copyright 2018 mayabot.com authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mayabot.nlp.segment;

import com.google.common.collect.Lists;
import com.mayabot.nlp.segment.tokenizer.CoreTokenizerBuilder;
import com.mayabot.nlp.segment.tokenizer.CrfTokenizerBuilder;
import com.mayabot.nlp.segment.tokenizer.WordnetTokenizer;
import com.mayabot.nlp.segment.tokenizer.WordnetTokenizerBuilder;

import java.util.List;
import java.util.function.Consumer;

/**
 * MynlpTokenizer切词器接口,只需要关心把一句话或一小段有限的文本分词完成.
 * 切词器的实现类是个无状态，所以可以多线程安全，同一个配置只需要一个实例共享使用。
 * 所以一个固定的算法的切分,只需要一个实例。
 * 通过MynlpTokenizerBuilder来创建MynlpTokenizer对象。
 *
 * @see WordnetTokenizer
 * @see MynlpTokenizerBuilder
 * @see WordnetTokenizerBuilder
 * @author jimichan
 */
public interface MynlpTokenizer {

    static MynlpTokenizer nlpTokenizer() {
        return new CoreTokenizerBuilder().build();
    }

    static MynlpTokenizer crfTokenizer() {
        return new CrfTokenizerBuilder().build();
    }

    /**
     * 对text进行分词，结构保存在List<WordTerm>,
     * token不会对target清空，只会add。
     *
     * @param text 分词的文本
     * @param consumer 对WordTerm对象消费者
     * @return
     */
    void token(char[] text, Consumer<WordTerm> consumer);


    /**
     * 对text是String字符串的一个便捷包装.
     *
     * @param text
     * @param target
     */
    default void token(String text, Consumer<WordTerm> target) {
        token(text.toCharArray(), target);
    }


    /**
     * 便捷方法。不适用于超大文本
     * @param text
     * @return
     */
    default List<String> tokenToStringList(String text) {
        if (text == null || text.isEmpty()) {
            return Lists.newArrayListWithCapacity(1);
        }
        List<String> target = Lists.newArrayListWithExpectedSize(text.length() / 2);

        token(text.toCharArray(), x -> target.add(x.word));

        return target;
    }

    /**
     * 便捷方法。不适用于超大文本
     *
     * @param text
     * @return
     */
    default List<WordTerm> tokenToTermList(String text) {
        if (text == null || text.isEmpty()) {
            return Lists.newArrayListWithCapacity(1);
        }
        List<WordTerm> target = Lists.newArrayListWithExpectedSize(text.length() / 2);

        token(text.toCharArray(), x -> target.add(x));
        return target;
    }

}
