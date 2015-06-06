package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * CSV を入出力するためのユーティリティークラス。
 * @author Ryo
 */
public class CsvUtils {

	public static final Charset CHARSET_MS932 = Charset.forName("MS932");
	public static final Charset CHARSET_SJIS = CHARSET_MS932;
	public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

	public static final String LF = "\n";
	public static final String CRLF = "\r\n";
	public static final String CR = "\r";

	public static final String DEFAULT_DELIMITER = ",";
	public static final String DEFAULT_QUOTE = "\"";
	public static final String DEFAULT_LF = LF;
	public static final Charset DEFAULT_CHARSET = CHARSET_UTF8;

	/**
	 * CSV ファイルを書き出す。
	 * @param file
	 * @param lines
	 */
	public static <T> void write(File file, List<List<T>> lines) {
		write(file, lines, null, DEFAULT_DELIMITER, DEFAULT_QUOTE, DEFAULT_LF, DEFAULT_CHARSET);
	}

	/**
	 * CSV ファイルを書き出す。
	 * @param file 出力ファイル
	 * @param lines 出力する行とトークンのリスト
	 * @param delimiter 区切り文字
	 */
	public static <T> void write(File file, List<List<T>> lines, String delimiter) {
		write(file, lines, null, delimiter, DEFAULT_QUOTE, DEFAULT_LF, DEFAULT_CHARSET);
	}

	/**
	 * CSV ファイルを書き出す。
	 * @param file 出力ファイル
	 * @param lines 出力する行とトークンのリスト
	 * @param delimiter 区切り文字
	 * @param quote 区切り引用符
	 */
	public static <T> void write(File file, List<List<T>> lines, String delimiter, String quote) {
		write(file, lines, null, delimiter, quote, DEFAULT_LF, DEFAULT_CHARSET);
	}

	/**
	 * CSV ファイルを書き出す。
	 * @param file 出力ファイル
	 * @param lines 出力する行とトークンのリスト
	 * @param delimiter 区切り文字
	 * @param quote 区切り引用符
	 * @param lf 改行コード
	 */
	public static <T> void write(File file, List<List<T>> lines, String delimiter, String quote,
			String lf) {
		write(file, lines, null, delimiter, quote, lf, DEFAULT_CHARSET);
	}

	/**
	 * CSV ファイルを書き出す。
	 * @param file 出力ファイル
	 * @param lines 出力する行とトークンのリスト
	 * @param delimiter 区切り文字
	 * @param quote 区切り引用符
	 * @param lf 改行コード
	 * @param outputCharset 出力文字コード
	 */
	public static <T> void write(File file, List<List<T>> lines, String delimiter,
			String quote, String lf, Charset outputCharset) {
		write(file, lines, null, delimiter, quote, lf, outputCharset);
	}

	/**
	 * CSV ファイルを書き出す。
	 * @param file
	 * @param lines
	 * @param headers ヘッダー
	 */
	public static <T> void write(File file, List<List<T>> lines, List<String> headers) {
		write(file, lines, headers, DEFAULT_DELIMITER, DEFAULT_QUOTE, DEFAULT_LF, DEFAULT_CHARSET);
	}

	/**
	 * CSV ファイルを書き出す。
	 * @param file 出力ファイル
	 * @param lines 出力する行とトークンのリスト
	 * @param headers ヘッダー
	 * @param delimiter 区切り文字
	 */
	public static <T> void write(File file, List<List<T>> lines, List<String> headers,
			String delimiter) {
		write(file, lines, headers, delimiter, DEFAULT_QUOTE, DEFAULT_LF, DEFAULT_CHARSET);
	}

	/**
	 * CSV ファイルを書き出す。
	 * @param file 出力ファイル
	 * @param lines 出力する行とトークンのリスト
	 * @param headers ヘッダー
	 * @param delimiter 区切り文字
	 * @param quote 区切り引用符
	 */
	public static <T> void write(File file, List<List<T>> lines, List<String> headers,
			String delimiter, String quote) {
		write(file, lines, headers, delimiter, quote, DEFAULT_LF, DEFAULT_CHARSET);
	}

	/**
	 * CSV ファイルを書き出す。
	 * @param file 出力ファイル
	 * @param lines 出力する行とトークンのリスト
	 * @param headers ヘッダー
	 * @param delimiter 区切り文字
	 * @param quote 区切り引用符
	 * @param lf 改行コード
	 */
	public static <T> void write(File file, List<List<T>> lines, List<String> headers,
			String delimiter, String quote,
			String lf) {
		write(file, lines, headers, delimiter, quote, lf, DEFAULT_CHARSET);
	}

	/**
	 * CSV ファイルを書き出す。
	 * @param file 出力ファイル
	 * @param lines 出力する行とトークンのリスト
	 * @param headers ヘッダー
	 * @param delimiter 区切り文字
	 * @param quote 区切り引用符
	 * @param lf 改行コード
	 * @param outputCharset 出力文字コード
	 */
	public static <T> void write(File file, List<List<T>> lines, List<String> headers,
			String delimiter, String quote, String lf, Charset outputCharset) {
		try (BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file), outputCharset))) {
			if(headers != null) {
				Iterator<String> headerIterator = headers.iterator();
				while(headerIterator.hasNext()) {
					String header = headerIterator.next();
					writeToken(bw, header, delimiter, quote, headerIterator.hasNext());
				}
				bw.write(lf);
			}
			Iterator<List<T>> lineIterator = lines.iterator();
			while(lineIterator.hasNext()) {
				List<T> line = lineIterator.next();
				Iterator<T> tokenIterator = line.iterator();
				while(tokenIterator.hasNext()) {
					Object tokenObject = tokenIterator.next();
					String token = tokenObject == null ? "\\N" : tokenObject.toString();
					writeToken(bw, token, delimiter, quote, tokenIterator.hasNext());
				}
				bw.write(lf);
			}
			bw.flush();
		} catch (IOException e) {
			throw new CsvHandlingException(
					"Unexpected exception occured while writing CSV file.", e);
		}
	}

	private static void writeToken(BufferedWriter bw, String token, String delimiter,
			String quote, Boolean hasNext) throws IOException {
		if(quote != null) {
			bw.write(quote);
			bw.write(token);
			bw.write(quote);
		} else {
			if(delimiter != null) {
				token = token.replace(delimiter, "\\" + delimiter);
			}
			bw.write(token);
		}
		if(hasNext && delimiter != null) {
			bw.write(delimiter);
		}
	}

	/**
	 * CSV ファイルを読み込む。
	 * @param file 入力ファイル
	 * @return 行とトークンのリスト
	 */
	public static List<List<String>> read(File file) {
		return read(file, DEFAULT_DELIMITER, DEFAULT_QUOTE, DEFAULT_CHARSET);
	}

	/**
	 * CSV ファイルを読み込む。
	 * @param file 入力ファイル
	 * @param delimiter 区切り文字
	 * @return 行とトークンのリスト
	 */
	public static List<List<String>> read(File file, String delimiter) {
		return read(file, delimiter, DEFAULT_QUOTE, DEFAULT_CHARSET);
	}

	/**
	 * CSV ファイルを読み込む。
	 * @param file 入力ファイル
	 * @param delimiter 区切り文字
	 * @param quote 区切り引用符
	 * @param inputCharset 入力文字コード
	 */
	public static List<List<String>> read(File file, String delimiter, String quote) {
		return read(file, delimiter, quote, DEFAULT_CHARSET);
	}

	/**
	 * CSV ファイルを読み込む。
	 * @param file 入力ファイル
	 * @param delimiter 区切り文字
	 * @param quote 区切り引用符
	 * @param inputCharset 入力文字コード
	 * @return 行とトークンのリスト
	 */
	public static List<List<String>> read(File file, String delimiter, String quote,
			Charset inputCharset) {
		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), inputCharset))) {
			List<List<String>> lines = new ArrayList<>();
			String line;
			while( (line = br.readLine()) != null) {
				List<String> tokens = new ArrayList<>();
				StringTokenizer tokenizer;
				if(quote == null) {
					tokenizer = new StringTokenizer(line, delimiter);
				} else {
					tokenizer = new StringTokenizer(
							line,
							new StringBuilder()
									.append(quote)
									.append(delimiter)
									.append(quote)
									.toString()
							);
				}
				boolean head = true;
				while(tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					if(quote != null) {
						if(head) {
							token = token.replaceFirst("^" + quote, "");
						} else if (!tokenizer.hasMoreTokens()) {
							token = token.replaceFirst(quote + "$", "");
						}
					}
					tokens.add(token);
					head = false;
				}
				lines.add(tokens);
			}
			return lines;
		}catch (IOException e) {
			throw new CsvHandlingException(
					"Unexpected exception occured while reading CSV file.", e);
		}
	}

	/**
	 * トークンのリストを CSV 文字列に変換する。
	 * @param tokens トークンのリスト
	 * @return CSV 文字列
	 */
	public static <T> String convertTokensToCsv(List<T> tokens) {
		return convertTokensToCsv(tokens, DEFAULT_DELIMITER, DEFAULT_QUOTE);
	}

	/**
	 * トークンのリストを CSV 文字列に変換する。
	 * @param tokens トークンのリスト
	 * @param delimiter 区切り文字
	 * @return CSV 文字列
	 */
	public static <T> String convertTokensToCsv(List<T> tokens, String delimiter) {
		return convertTokensToCsv(tokens, delimiter, DEFAULT_QUOTE);
	}

	/**
	 * トークンのリストを CSV 文字列に変換する。
	 * @param tokens トークンのリスト
	 * @param delimiter 区切り文字
	 * @param quote 区切り引用符
	 * @return CSV 文字列
	 */
	public static <T> String convertTokensToCsv(List<T> tokens, String delimiter, String quote) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for(T tokenObject : tokens) {
			String token;
			if(tokenObject == null) {
				token = "\\N";
			} else if (tokenObject instanceof String) {
				token = (String) tokenObject;
			} else {
				token = tokenObject.toString();
			}

			if(delimiter != null) {
				if(quote == null) {
					// add escape char to delimiter
					token = token.replace(delimiter, "\\" + delimiter);
				}

				if(isFirst) {
					// skip delimieter for first token
					isFirst = false;
				} else {
					// add delimieter
					sb.append(delimiter);
				}
			}

			if(quote != null && tokenObject != null) {
				sb.append(quote).append(token).append(quote);
			} else {
				sb.append(token);
			}
		}
		return sb.toString();
	}

	/**
	 * CSV 操作中の例外。
	 * @author Ryo
	 */
	public static class CsvHandlingException extends RuntimeException {

		private static final long serialVersionUID = 6847163493114842527L;

		public CsvHandlingException(String message, Throwable cause) {
			super(message, cause);
		}

		public CsvHandlingException(String message) {
			super(message);
		}

		public CsvHandlingException(Throwable cause) {
			super(cause);
		}

	}

}
