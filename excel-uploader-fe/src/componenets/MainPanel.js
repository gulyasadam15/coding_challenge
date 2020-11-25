import { Button, Input, Table } from 'antd';
import React from 'react';
import axios from 'axios';
import './MainPanel.css';

class MainPanel extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
        columns : [
            {
              title: 'Passenger Class',
              dataIndex: 'passengerClass',
              key: 'passengerClass'
           },
           
           {
              title: 'Name',
              dataIndex: 'name',
              key: 'name'
           },
           
           {
              title: 'Sex',
              dataIndex: 'sex',
              key: 'sex'
           },
           
           {
              title: 'Age',
              dataIndex: 'age',
              key: 'age'
           },
           
           {
              title: 'No of Siblings or Spouses on Board',
              dataIndex: 'noOfSiblingsOrSpousesOnBoard',
              key: 'noOfSiblingsOrSpousesOnBoard'
           },
           
           {
              title: 'No of Parents or Children on Board',
              dataIndex: 'noOfParentsOrChildrenOnBoard',
              key: 'noOfParentsOrChildrenOnBoard'
           },
           
           {
              title: 'Ticket Number',
              dataIndex: 'ticketNumber',
              key: 'ticketNumber'
           },
           
           {
              title: 'Passenger Fare',
              dataIndex: 'passengerFare',
              key: 'passengerFare'
           },
           
           {
              title: 'Cabin',
              dataIndex: 'cabin',
              key: 'cabin'
           },
           
           {
              title: 'Port of Embarkation',
              dataIndex: 'portOfEmbarkation',
              key: 'portOfEmbarkation'
           },
           
           {
              title: 'Life Boat',
              dataIndex: 'lifeBoat',
              key: 'lifeBoat'
           },
           
           {
              title: 'Survived',
              dataIndex: 'survived',
              key: 'survived'
           }
          ],
        
          fileData : []
      };
    }

    preview(event){
        let files = event.target.files;
        if (files.length === 0)
            return;
        if(files[0].type!=='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'){
            alert('Only xlsx files allowed!');
            return;
        }
        this.setState({
            selectedFile: files[0] 
        });
    }

    uploadFile(){
        if(this.state.selectedFile){
            this.setState({
                fileProcessingState : "UPLOADING"
            });
            const data = new FormData();
            data.append('file', this.state.selectedFile);
            axios.post("api/documents/upload", data, {  
                })
                .then(res => { 
                    this.setState({
                        fileProcessingState : res.data.documentProcessingState,
                        uploadedDocumentId : res.data.documentId
                    });
                    if(res.data.documentId){
                        this.startDocumentStatePulling();
                    }
                })
                .catch(()=>{
                    this.setState({
                        fileProcessingState : 'UPLOAD_ERROR'
                    });
                });

        }
    }

    startDocumentStatePulling(){
        setInterval(()=> { 
            if(this.state.uploadedDocumentId && this.state.fileProcessingState !== 'CONVERTED' && this.state.fileProcessingState !== 'CONVERTION_ERROR'){
                axios.get('api/documents/documentProcessingState/'+this.state.uploadedDocumentId)
                    .then(res => {
                        this.setState({
                            fileProcessingState : res.data.documentProcessingState
                        });
                    });
            }
         }, 1 * 1000); 
    }

    showResult(){
        if(this.state.uploadedDocumentId && this.state.fileProcessingState === 'CONVERTED'){
            axios.get('api/documents/documentContent/'+this.state.uploadedDocumentId)
                .then(res => {
                    this.setState({
                        fileData : res.data.document
                    });
                });
        }
    }
    
    render() {
      return (
        <div class="main-panel">
            <div class="control-panel">
                <div class="file-uploader">
                    <Input type="file" onChange={event => this.preview(event)} accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
                    <Button onClick={()=> this.uploadFile()}>Upload</Button>
                </div>
                <div class="status-bar"> 
                    {this.state.fileProcessingState}
                    {this.state.fileProcessingState === 'CONVERTED' ? <Button onClick={()=> this.showResult()}>Show Result</Button> : null}
                </div>
            </div>
            <div class="result-panel">
                <Table dataSource={this.state.fileData} columns={this.state.columns} />
            </div>
        </div>
      );
    }
  }

  export default MainPanel;
  