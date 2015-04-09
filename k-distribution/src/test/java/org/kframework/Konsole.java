package org.kframework;

import org.kframework.attributes.Source;
import org.kframework.definition.Module;
import org.kframework.kore.K;
import org.kframework.kompile.Kompile;
import org.kframework.tiny.Rewriter;
import org.kframework.utils.file.FileUtil;
import scala.Tuple2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.function.BiFunction;

public class Konsole {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String definitionFilename = args[0];
        String mainModuleName = args[1];
        String programModuleName = args[2];

        Tuple2<Module, BiFunction<String, Source, K>> stuff =
                new Kompile(FileUtil.testFileUtil()).run(new File(definitionFilename), mainModuleName, programModuleName, "K");

        Module module = stuff._1();
        BiFunction<String, Source, K> programParser = stuff._2();
        Rewriter rewriter = new org.kframework.tiny.Rewriter(module);
        String cmd;

        do {
            System.out.print(">");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            cmd = br.readLine();
            if (cmd.startsWith("rw")) {
                K result = rewriter.execute(programParser.apply(cmd.substring(2), Source.apply("<command line>")));
                System.out.println("=> " + result);
            } else if (cmd.equals("exit")) {
                break;
            } else
                System.out.println("Unknown command.");
        } while (true);
        System.out.println("Bye!");
    }
}
