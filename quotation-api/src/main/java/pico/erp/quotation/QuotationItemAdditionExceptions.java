package pico.erp.quotation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface QuotationItemAdditionExceptions {

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "quotation.item.addition.already.exists.exception")
  class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "quotation.item.addition.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }
}
