package com.bow.adaptive;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.validation.Validation;

/**
 * @author vv
 * @since 2017/1/13.
 */
public class Validation$Adpative implements Validation {
    public com.alibaba.dubbo.validation.Validator getValidator(com.alibaba.dubbo.common.URL arg0) {
        if (arg0 == null)
            throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg0;
        String extName = url.getParameter("validation", "jvalidation");
        if (extName == null)
            throw new IllegalStateException(
                    "Fail to get extension(com.alibaba.dubbo.validation.Validation) name from url(" + url.toString()
                            + ") use keys([validation])");
        Validation extension = ExtensionLoader.getExtensionLoader(Validation.class).getExtension(extName);
        return extension.getValidator(arg0);
    }
}
