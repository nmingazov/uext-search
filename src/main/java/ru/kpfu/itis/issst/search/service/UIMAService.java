package ru.kpfu.itis.issst.search.service;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.XMLSerializer;
import org.springframework.stereotype.Service;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.ExternalResourceFactory;
import org.uimafit.factory.JCasFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.uimafit.pipeline.SimplePipeline;
import org.uimafit.util.JCasUtil;
import org.xml.sax.SAXException;
import ru.kfu.cll.uima.segmentation.SentenceSplitter;
import ru.kfu.cll.uima.tokenizer.InitialTokenizer;
import ru.kfu.cll.uima.tokenizer.PostTokenizer;
import ru.kfu.itis.issst.uima.morph.commons.TagAssembler;
import ru.ksu.niimm.cll.uima.morph.opencorpora.MorphologyAnnotator;
import ru.ksu.niimm.cll.uima.morph.opencorpora.resource.ConfigurableSerializedDictionaryResource;
import ru.ksu.niimm.cll.uima.morph.opencorpora.resource.DummyWordformPredictor;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
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

    /**
     * Инициализирует использующийся pipeline
     * @throws ResourceInitializationException
     */
    @PostConstruct
    public void initialization() throws ResourceInitializationException {
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

        analysisEngine = AnalysisEngineFactory.createAggregate(
                AnalysisEngineFactory.createAggregateDescription(
                    InitialTokenizer.createDescription(),
                    PostTokenizer.createDescription(),
                    SentenceSplitter.createDescription(),
                    MorphologyAnnotator.createDescription(morphDictDesc),
                    TagAssembler.createDescription(morphDictDesc)
                )
        );
    }

    /**
     * Запускает пайплайн, затем, используя грязные методы, выдирает информацию об аннотациях
     * @param text исходный текст
     * @throws UIMAException
     * @throws IOException
     */
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

    /**
     * Запускает пайплайн, затем преобразует JCas в Xmi
     * @param text исходный текст
     * @throws UIMAException
     * @throws IOException
     * @throws SAXException
     */
    public String getXmlTranslatedResult(String text) throws UIMAException, IOException, SAXException {
        JCas jCas = JCasFactory.createJCas(typeSystemDescription);
        jCas.setDocumentText(text);

        SimplePipeline.runPipeline(jCas, analysisEngine);

        // Xmi Consumer Logic
        ByteArrayOutputStream xmiOutputStream = new ByteArrayOutputStream();

        XmiCasSerializer ser = new XmiCasSerializer(jCas.getTypeSystem());
        XMLSerializer xmlSer = new XMLSerializer(xmiOutputStream, true);

        ser.serialize(jCas.getCas(), xmlSer.getContentHandler());

        return xmiOutputStream.toString("UTF-8");
    }
}
