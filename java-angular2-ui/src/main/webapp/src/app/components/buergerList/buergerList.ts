import { Component, View, Input } from 'angular2/core';
import { NgForm, Validators, FormBuilder} from 'angular2/common';
import {BuergerService} from '../../services/buergerService';

@Component({})
@View({
	templateUrl: 'app/components/buergerList/buergerList.html',
	directives: []
})
export class BuergerList {
  buerger: Array<any>;
  buergerForm;
  @Input() newSearchTerm: string;
  constructor(private buergerService: BuergerService, private fb: FormBuilder) {
        this.buergerForm = fb.group({
            nachname: ["", Validators.required],
            vorname: ["", Validators.required],
            geburtsdatum: ["", Validators.required],
            augenfarbe: ["", Validators.required],
            alive: ["", Validators.required],
            eigenschaften: ["", Validators.required]
        });    
        this.loadBuergers();
    }
    
    loadBuergers(){
            this.buergerService.getBuergers()
                .subscribe(
                    res => this.buerger = res.json()._embedded.buergers,
                    err => console.log(err),
                    () => console.log('Buerger loaded: ' + JSON.stringify(this.buerger))
                );
    }

    search(){
        this.buergerService.searchBuerger(this.newSearchTerm)
               .subscribe(
                    res => this.buerger = res.json()._embedded.buergers,
                    err => console.log(err),
                    () => console.log('Buerger loaded: ' + JSON.stringify(this.buerger))
                );
        this.newSearchTerm = '';
    }

    addBuerger(){
        console.log(this.buergerForm.value);
        this.buergerForm.value.eigenschaften = ['test'];
        var values = JSON.stringify(this.buergerForm.value);
        this.buergerService.addBuerger(values).subscribe(
            err => console.log(err),
            () => this.loadBuergers()
        );
    }

}
