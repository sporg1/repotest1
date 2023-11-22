package main.java.com.journaldev.examples;

import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sasanlabs.internal.utility.LevelConstants;
import org.sasanlabs.internal.utility.Variant;
import org.sasanlabs.internal.utility.annotations.AttackVector;
import org.sasanlabs.internal.utility.annotations.VulnerableAppRequestMapping;
import org.sasanlabs.internal.utility.annotations.VulnerableAppRestController;
import org.sasanlabs.service.vulnerability.bean.GenericVulnerabilityResponseBean;
import org.sasanlabs.service.vulnerability.xxe.bean.Book;
import org.sasanlabs.service.vulnerability.xxe.bean.ObjectFactory;
import org.sasanlabs.service.vulnerability.xxe.dao.BookEntity;
import org.sasanlabs.service.vulnerability.xxe.dao.BookEntityRepository;
import org.sasanlabs.vulnerability.types.VulnerabilityType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.springframework.oxm.xstream.XStreamMarshaller;
import com.thoughtworks.xstream.XStream;


public class BookInfo {

    private static final transient Logger LOGGER =
            LogManager.getLogger(PathTraversalVulnerability.class);

    private static final String URL_PARAM_KEY = "url";

    public ResponseEntity<GenericVulnerabilityResponseBean<Book>> saveXStreamBasedBookInformation(
            @RequestBody HttpServletRequest request, XStreamMarshaller xstreamMarshaller, String level) {
        try {
            XStream xstream = xstreamMarshaller.getXStream();
            InputStream in = request.getInputStream();
            Book book = (Book) xstream.fromXML(in);
            BookEntity bookEntity = new BookEntity(book, level);
            bookEntityRepository.save(bookEntity);
            return new ResponseEntity<GenericVulnerabilityResponseBean<Book>>(
                    new GenericVulnerabilityResponseBean<Book>(book, true),
                    HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new ResponseEntity<GenericVulnerabilityResponseBean<Book>>(
                new GenericVulnerabilityResponseBean<Book>(null, false), HttpStatus.OK);
    }

    public ResponseEntity<GenericVulnerabilityResponseBean<Book>> getVulnerablePayloadLevel1(
            HttpServletRequest request, Jaxb2Marshaller jaxb2Marshaller) {
        try {
            InputStream in = request.getInputStream();
            JAXBElement<Book> bookJaxbElement = (JAXBElement<Book>) jaxb2Marshaller.unmarshal(new StreamSource(in));
            BookEntity bookEntity = new BookEntity(bookJaxbElement.getValue(), LevelConstants.LEVEL_1);
            bookEntityRepository.save(bookEntity);
            return new ResponseEntity<GenericVulnerabilityResponseBean<Book>>(
                    new GenericVulnerabilityResponseBean<Book>(bookJaxbElement.getValue(), true),
                    HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new ResponseEntity<GenericVulnerabilityResponseBean<Book>>(
                new GenericVulnerabilityResponseBean<Book>(null, false), HttpStatus.OK);
    }




}
