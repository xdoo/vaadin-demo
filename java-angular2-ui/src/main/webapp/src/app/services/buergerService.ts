import {Inject, Injectable} from 'angular2/core';
import {Http, Headers} from 'angular2/http';

@Injectable()
export class BuergerService {
	
	constructor(@Inject(Http) private http: Http) {		
	}
	
	searchBuerger(param: string): any{
		return this.http.get("/buergers/search/findFullTextFuzzy?q="+param);
	}
	
	getBuerger(id: string): any{
		return this.http.get("/buergers/"+id);
	}
	
	getBuergers(): any{
		return this.http.get("/buergers/");
    }
    
    addBuerger(buerger:any): any{
        var headers = new Headers();
        headers.append('Content-Type', 'application/json');
        return this.http.post("/buergers/",buerger, {headers:headers});
    }
}