package com.nplit.exception;

/**
 * �̸��� ������ ���� ���� �� �ͼ���.
 * @author Park
 *
 */
public class FailedSendMailException extends ResponseBodyException {
   private static final long serialVersionUID = 1L;

   public FailedSendMailException(String message, Throwable cause) {
      super(message, 100, cause);
   }
}