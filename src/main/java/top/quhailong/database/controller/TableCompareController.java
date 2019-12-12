package top.quhailong.database.controller;

import top.quhailong.database.entity.TableCompareRequest;
import top.quhailong.database.provider.TableCompareProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableCompareController {

    @Autowired
    private TableCompareProvider tableCompareProvider;

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String TableCompare(@RequestBody TableCompareRequest request) {
        tableCompareProvider.tableCompareHandle(request);
        return "done";
    }
}
