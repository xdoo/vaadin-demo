package de.muenchen.vaadin.ui.util;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.datefield.Resolution;
import java.util.Date;

/**
 * Created by arne.schoentag on 17.08.15.
 */
public class ValidatorFactory {
    public static Validator getValidator(String kind,String... args){
        if(kind == null)
            return null;
        switch(kind){
            case "StringLength":
                if(args.length<4)
                       return null;
                return new StringLengthValidator(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]),Boolean.parseBoolean(args[3]));
            case "DateRange"    :
                if(args.length<3)
                    return null;
                return new DateRangeValidator(args[0],new Date(args[1]==null ? null: Integer.parseInt(args[2])),new Date(args[2]==null ? null: Integer.parseInt(args[2])),Resolution.YEAR);    
            case "IntegerRange" :
                if(args.length<3)
                    return null;
                return new IntegerRangeValidator(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]));
            case "Null":
                if(args.length<2)
                    return null;
                return new NullValidator(args[0],Boolean.parseBoolean(args[1]));
            default:
                return null;
        }
    }
}
