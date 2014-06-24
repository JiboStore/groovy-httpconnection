@GrabConfig(systemClassLoader=true)
@Grapes(
	@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )
)

//compile('org.codehaus.groovy.modules.http-builder:http-builder:0.5.0') {
//        excludes "commons-logging", "xml-apis", "groovy"
//}

import groovyx.net.http.HTTPBuilder;
import static groovyx.net.http.Method.GET;
import static groovyx.net.http.ContentType.TEXT;
import groovy.text.GStringTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;

public class MainEntry {
	
	private static final char[] LOOKUPBASE64ALPHABET = new char[64];
	public Set<PrintWriter> outputWriter = [ System.out ]

	// usage: groovy URLConnector headers.properties http://www.url.com/params?q=value&s=othervalue
	public void mainEntry(String[] args) {
		String headerFilename = "headers.properties"
		if ( args == null || args.length < 2 ) {
			printUsage()
		} else {
			headerFilename = args[0]
		}
		HashMap<String, String> headerMap = new HashMap<String, String>()
		File headerFile = new File( headerFilename )
		headerFile.eachLine {
			println "${it}"
			int iIndexKey = it.indexOf(':')
			if ( iIndexKey != -1 ) {
				String szKey = it.substring(0, iIndexKey)
				String szValue = it.substring( iIndexKey + 1 )
				if ( szKey.charAt(0) != '#' ) {
					println( "${szKey} => ${szValue}" )
					headerMap.put( szKey.trim(), szValue.trim() )
				}
			}
		}
		
		String szTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
		String szImei = "357945051088743"
		String szSecurity = getSecurityHeader(args[1], szTime, szImei)
		String szSecurityEncoded = URLEncoder.encode(szSecurity)
		println( "security: ${szSecurity} => ${szSecurityEncoded}" )
		headerMap.put( "security", szSecurityEncoded )
		headerMap.put( "timestamp", szTime )
		
//		org.apache.http.headers.priority = "DEBUG"
//		org.apache.http.wire.priority = "DEBUG"
		java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
		java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
		
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "debug");
		
		HTTPBuilder http = new HTTPBuilder( args[1] )
		http.setHeaders(headerMap)
		http.request(GET, TEXT) { req ->
//			headerMap.collectEntries { key, value ->
//				println( "header: ${key} -> ${value}" )
//				headers."${key}" = "${value}"
//			}
			
			response.success = { resp, reader ->
				assert resp.status == 200
				println( "response status: ${resp.statusLine}" )
				println( "response length: ${resp.headers.'Content-Length'}" )
				System.out << reader
			}
		}
	}
	
	public void printUsage() {
		writeOutput( "Usage:" )
		writeOutput( "groovy MainEntry.groovy headers.properties http://112.215.81.113:80/tpf/i/getdownloadURL.action?productID=1000016870&contentCode=001810000000147353")
	}
	
	public void writeOutput(String msg, Exception e = null) {
//		println "outputWriter: ${outputWriter.size()}"
		outputWriter.each {
			it.println msg
			if ( e ) {
				e.printStackTrace(it)
			}
		}
		outputWriter*.flush()
	}
	
	public static String getSecurityHeader(String szUrl, String szDateTime, String szImei)
	{
//		String str1 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//		String str2 = "6281234567890";
		String str1 = szDateTime;
		String str2 = szImei;
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append(szUrl).append("Huawei_ODP").append("portalone").append(str1).append(str2);
		return EncryptBase64(localStringBuffer.toString());
	}
	
	public static String EncryptBase64(String paramString)
	{
			String str = Base64.encodeBase64String(MessageDigest.getInstance("SHA-256").digest(paramString.getBytes("UTF-8")));
			return str;
	}

/* 	//public Set<PrintWriter> outputWriter = [ System.out ]
	public Set<PrintWriter> outputWriter = new HashSet<PrintWriter>();
	
	public File templateFile = new File( 'entry.template' )
	public GStringTemplateEngine templateEngine = new GStringTemplateEngine()

	public void cmd(String[] args) {
		String defaultDir = "testlog"
		if ( args == null || args.length == 0 ) {
			printUsage();
			defaultDir = "testlog"
		} else {
			defaultDir = args[0]
		}
		outputWriter << new File( "output.html" ).newPrintWriter()
			   writeOutput( "<html><table table-layout=\"fixed\" border=\"1\">" )
			writeOutput( "<tr><th>Package Name</th><th>Device ID</th><th>Non Wrapped Launch:</th><th>Non Wrapped Logcat:</th><th>Non Wrapped Test:</th><th>Wrapped Launch:</th><th>Wrapped Lock:</th><th>Wrapped Unlock:</th><th>Wrapped Logcat:</th><th>Wrapped Test:</th></tr>" )
		File workDir = new File(defaultDir)
		workDir.listFiles().each {
//			println "${it.path} > ${it.directory}"
			if ( it.directory && it.path.toLowerCase().contains("apk") ) {
				traverseApk( it )
			}
		}
			writeOutput( "</table></html>" )
	}
	
	public void printUsage() {
		writeOutput( "Usage: groovy GenerateResult.groovy testlogdir" )
	}
	
	public void traverseApk(File apkDir) {
//		println apkDir.path
		def packageName, deviceId
		apkDir.listFiles().each {
//			println "helloworld".substring( "helloworld".lastIndexOf('o') )
//			packageName = it.path.substring( it.path.lastIndexOf('\\') + 1 )
//			println "${packageName} > ${it.directory}"
			if ( it.directory ) {
				traverseApkDevice( it )
			}
		}
	}
	
	public void traverseApkDevice(File deviceDir) {
		deviceDir.listFiles().each() {
//			println it.path
			int iDeviceId = it.path.lastIndexOf('\\')
			int iPackageName = it.path.lastIndexOf('\\', iDeviceId-1)
			String deviceId = it.path.substring( iDeviceId + 1 )
			String packageName = it.path.substring( iPackageName + 1, iDeviceId )
			String nonwrappedLaunch, nonwrappedLogcat, nonwrappedTest
			String wrappedLaunch, wrappedLock, wrappedUnlock, wrappedLogcat, wrappedTest
			File nonwrappedDir = new File( it, 'nonwrapped' )
			File wrappedDir = new File( it, 'wrapped' )
			
			nonwrappedDir.listFiles().each() {
				if ( it.path.contains( 'launch.png' ) ) {
					nonwrappedLaunch = it.path
				} else if ( it.path.contains( 'logcat.log' ) ) {
					nonwrappedLogcat = it.path
				} else if ( it.path.contains( 'test.log' ) ) {
					nonwrappedTest = it.path
				}
			}
			
			wrappedDir.listFiles().each() {
				println it.path
				if ( it.path.contains( 'launch.png' ) ) {
					wrappedLaunch = it.path
				} else if ( it.path.contains( 'unlock.png' ) ) {
					wrappedUnlock = it.path
				} else if ( it.path.contains( 'lock.png' ) ) {
					wrappedLock = it.path
				} else if ( it.path.contains( 'logcat.log' ) ) {
					wrappedLogcat = it.path
				} else if ( it.path.contains( 'test.log' ) ) {
					wrappedTest = it.path
				}
			}
			
			def binding = [
				"packageName" : packageName,
				"deviceId" : deviceId,
				"nonwrappedLaunch" : htmlTagImage(nonwrappedLaunch),
				"nonwrappedLogcat" : htmlTagLogfile(nonwrappedLogcat),
				"nonwrappedTest" : htmlTagLogfile(nonwrappedTest),
				"wrappedLaunch" : htmlTagImage(wrappedLaunch),
				"wrappedLock" : htmlTagImage(wrappedLock),
				"wrappedUnlock" : htmlTagImage(wrappedUnlock),
				"wrappedLogcat" : htmlTagLogfile(wrappedLogcat),
				"wrappedTest" : htmlTagLogfile(wrappedTest)
			];
			
			writeOutput( templateEngine.createTemplate(templateFile).make(binding).toString() )
			
//			println "${it.path} > ${iDeviceId} : ${iPackageName}"
//			println "${packageName} > ${deviceId}"
		}
	}
	
	public void writeOutput(String msg, Exception e = null) {
//        	println "outputWriter: ${outputWriter.size()}"
		outputWriter.each {
			it.println msg
			if ( e ) {
				e.printStackTrace(it)
			}
		}
		outputWriter*.flush()
	}
	
	private String htmlTagImage( String imagePath ) {
		String htmlImg = imagePath == null ? "<br/>" : "<img src=\"${imagePath}\" width=\"240\" height=\"320\"/>"
		return htmlImg;
		//<img src="$nonwrappedLaunch"/>
	}
	
	private String htmlTagLogfile( String logPath ) {
		String htmlLog = logPath == null ? "<br/>" : "<a href=\"${logPath}\"> ${logPath.substring(logPath.lastIndexOf('\\')+1)} </a>"
		return htmlLog;
	}

	public static void main(String[] args) {
//		def file = new File('entry.template');
//		def engine = new GStringTemplateEngine();
//		args.each {
//		    def binding = ["packageName" : it, "fileName" : it, "deviceName" : it]
//		    println engine.createTemplate(file).make(binding).toString()
//		}
		new GenerateResult().cmd(args);
	} */
	
	public static void main(String[] args) {
		new MainEntry().mainEntry(args);
	}
}

