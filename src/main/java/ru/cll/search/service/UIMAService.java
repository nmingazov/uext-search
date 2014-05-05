package ru.cll.search.service;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.springframework.stereotype.Service;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.ExternalResourceFactory;
import org.uimafit.factory.JCasFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.uimafit.pipeline.SimplePipeline;
import org.uimafit.util.JCasUtil;
import ru.kfu.cll.uima.segmentation.SentenceSplitter;
import ru.kfu.cll.uima.tokenizer.InitialTokenizer;
import ru.kfu.cll.uima.tokenizer.PostTokenizer;
import ru.kfu.itis.issst.uima.morph.commons.TagAssembler;
import ru.ksu.niimm.cll.uima.morph.opencorpora.MorphologyAnnotator;
import ru.ksu.niimm.cll.uima.morph.opencorpora.resource.ConfigurableSerializedDictionaryResource;
import ru.ksu.niimm.cll.uima.morph.opencorpora.resource.DummyWordformPredictor;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

/**
 * author: Nikita
 * since: 05.05.2014
 */
@Service
public class UIMAService {
    public static final String MORPH_DICT_URL = "file:dict.opcorpora.ser";

    private TypeSystemDescription typeSystemDescription;
    private AnalysisEngine analysisEngine;

    @PostConstruct
    public void initialization() throws Exception {
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription(
                "ru.kfu.itis.cll.uima.commons.Commons-TypeSystem",
                "ru.kfu.cll.uima.tokenizer.tokenizer-TypeSystem",
                "ru.kfu.cll.uima.segmentation.segmentation-TypeSystem",
                "org.opencorpora.morphology-ts");
        typeSystemDescription = CasCreationUtils.mergeTypeSystems(Arrays.asList(tsd));

        ExternalResourceDescription morphDictDesc = ExternalResourceFactory.createExternalResourceDescription(
                ConfigurableSerializedDictionaryResource.class, MORPH_DICT_URL,
                ConfigurableSerializedDictionaryResource.PARAM_PREDICTOR_CLASS_NAME,
                DummyWordformPredictor.class.getName()
        );

        AnalysisEngineDescription analysisEngineDescription = AnalysisEngineFactory.createAggregateDescription(
                InitialTokenizer.createDescription(),
                PostTokenizer.createDescription(),
                SentenceSplitter.createDescription(),
                MorphologyAnnotator.createDescription(morphDictDesc),
                TagAssembler.createDescription(morphDictDesc));

        analysisEngine = AnalysisEngineFactory.createAggregate(analysisEngineDescription);
    }

    public String getAllAnnotationsAsString(String text) throws UIMAException, IOException {
        JCas jCas = JCasFactory.createJCas(typeSystemDescription);
        jCas.setDocumentText(text);

        SimplePipeline.runPipeline(jCas, analysisEngine);

        StringBuilder sb = new StringBuilder();
        for(Annotation annotation : JCasUtil.iterate(jCas, Annotation.class)){
            sb.append(annotation.toString())
               .append("Sofa:-----|")
               .append(annotation.getSofa().getLocalStringData().substring(annotation.getBegin(), annotation.getEnd()))
               .append("|-----\n");
        }
        return sb.toString();
    }

}
