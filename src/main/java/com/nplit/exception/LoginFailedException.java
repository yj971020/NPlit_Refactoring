package com.nplit.exception;

/**
 * �α����� ���� ���� �� �ͼ���.
 * @author Park
 *
 */
public class LoginFailedException extends ResponseBodyException {
   private static final long serialVersionUID = 1L;

   public LoginFailedException(String message, Throwable cause) {
      super(message, 200, cause);
   }
}