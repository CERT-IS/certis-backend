package certis.CertisHomepage.common.exception;

import certis.CertisHomepage.common.error.ErrorCodeIfs;

public interface ApiExceptionIfs {
    ErrorCodeIfs getErrorCodeIfs();
    String getErrorDescription();
}
