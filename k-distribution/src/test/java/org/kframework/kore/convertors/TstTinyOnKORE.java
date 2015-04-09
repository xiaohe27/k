// Copyright (c) 2014-2015 K Team. All Rights Reserved.

package org.kframework.kore.convertors;

import org.junit.Test;
import org.junit.rules.TestName;
import org.kframework.attributes.Source;
import org.kframework.definition.Module;
import org.kframework.kore.K;
import org.kframework.kompile.Kompile;
import org.kframework.tiny.Rewriter;
import org.kframework.utils.file.FileUtil;
import scala.Tuple2;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.BiFunction;


public class TstTinyOnKORE {


    @org.junit.Rule
    public TestName name = new TestName();


    protected File testResource(String baseName) {
        return new File(new File("k-distribution/src/test/resources" + baseName)
                .getAbsoluteFile().toString().replace("k-distribution" + File.separator + "k-distribution", "k-distribution"));
        // a bit of a hack to get around not having a clear working directory
        // Eclipse runs tests within k/k-distribution, IntelliJ within /k
    }

    @Test
    public void kore_imp_tiny() throws IOException, URISyntaxException {

        String filename = "/convertor-tests/" + name.getMethodName() + ".k";

        File definitionFile = testResource(filename);
        Tuple2<Module, BiFunction<String, Source, K>> rwModuleAndProgramParser = new Kompile(FileUtil.testFileUtil()).run(definitionFile, "TEST", "TEST-PROGRAMS", "K");

        Module module = rwModuleAndProgramParser._1();
        BiFunction<String, Source, K> programParser = rwModuleAndProgramParser._2();
        Rewriter rewriter = new org.kframework.tiny.Rewriter(module);

        K program = programParser.apply(
                "<top><k> while(0<=n) { s = s + n; n = n + -1; } </k><state>n|->10 s|->0</state></top>", Source.apply("generated by " + getClass().getSimpleName()));

        long l = System.nanoTime();
        K result = rewriter.execute(program);
        System.out.println("time = " + (System.nanoTime() - l) / 1000000);

        System.out.println("result = " + result.toString());
    }

}
