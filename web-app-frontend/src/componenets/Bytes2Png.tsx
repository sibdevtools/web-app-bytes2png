import React, { useState } from 'react';
import { base64ToFile, fileToBase64Stream } from '../utils/converters';
import { Alert, Button, Col, Container, Form, InputGroup, Row, Spinner } from 'react-bootstrap';
import { Download04Icon } from 'hugeicons-react';
import { decode, encode } from '../api/service';
import { formatFileSize } from '../utils/file';

enum Mode {
  ENCODE = 'To PNG',
  DECODE = 'From PNG'
}

export const Bytes2Png = () => {
  const [errorMessage, setErrorMessage] = useState('');
  const [width, setWidth] = useState<number | null>(null);
  const [height, setHeight] = useState<number | null>(null);
  const [useGZIP, setUseGZIP] = useState(true);
  const [base64, setBase64] = useState('');
  const [mode, setMode] = useState<Mode>(Mode.ENCODE);
  const [filename, setFilename] = useState('download.png');
  const [isLoading, setIsLoading] = useState(false);
  const [fileSize, setFileSize] = useState(0);

  const handleFileChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files) {
      await handleConvert(event?.target?.files[0]);
    }
  };

  const handleConvert = async (file: File | null) => {
    if (!file) {
      return;
    }
    setErrorMessage('')
    setIsLoading(true)
    setFileSize(0);
    try {
      const result = await fileToBase64Stream(file);
      setBase64(result);
      setFileSize(file.size);
    } catch (e: any) {
      setErrorMessage(`Loading error: ${e.message ?? e}`)
    } finally {
      setIsLoading(false);
    }
  };

  const doEncode = async () => {
    const rqWidth = width === 0 ? null : width;
    const rqHeight = height === 0 ? null : height;
    if (!base64) {
      setErrorMessage('Content is not loaded');
      return;
    }
    const rs = await encode({
      width: rqWidth,
      height: rqHeight,
      content: base64,
      useGZIP: useGZIP
    });
    if (rs.data.success) {
      const result = rs.data.body;
      base64ToFile(result, filename);
      setErrorMessage('');
    } else {
      console.error(`Failed to process: ${rs.status}`);
      setErrorMessage(`Failed to process: ${rs.status}`);
    }
  };

  const doDecode = async () => {
    const rs = await decode({ content: base64, useGZIP });
    if (rs.data.success) {
      const result = rs.data.body;
      base64ToFile(result, filename);
      setErrorMessage('');
    } else {
      console.error(`Failed to process: ${rs.status}`);
      setErrorMessage(`Failed to process: ${rs.status}`);
    }
  };

  const handleDownload = async () => {
    setIsLoading(true);
    try {
      if (mode === Mode.ENCODE) {
        await doEncode();
      } else {
        await doDecode();
      }
    } catch (e) {
      console.error(`Failed to process: ${e}`, e);
      setErrorMessage(`Failed to process: ${e}`);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container className="mt-5">
      <p className={'h2 mb-4'}>Bytes &lt;-&gt; PNG Converter</p>
      {errorMessage && (
        <Row>
          <Row className="mt-3">
            <Col>
              <Alert variant={'danger'} role="alert">
                {errorMessage}
              </Alert>
            </Col>
          </Row>
        </Row>
      )}
      <Row className="mb-3 align-items-center">
        <Col md={1}>
          <Form.Label htmlFor="fileInput">File</Form.Label>
        </Col>
        <Col md={11}>
          <InputGroup>
            <Form.Control
              id={'fileInput'}
              type="file"
              onChange={handleFileChange}
              disabled={isLoading}
            />
            <InputGroup.Text title={`${fileSize} bytes`}>
              {formatFileSize(fileSize)}
            </InputGroup.Text>
          </InputGroup>
        </Col>
      </Row>
      <Row className="mb-3 align-items-center">
        <Col xs={12} md={1}>
          <Form.Label htmlFor="modeSelect">Mode</Form.Label>
        </Col>
        <Col xs={12} md={11}>
          <InputGroup>
            <Form.Select
              id="modeSelect"
              value={mode}
              onChange={e => setMode(e.target.value as Mode)}
              disabled={isLoading}
            >
              {Object.values(Mode).map(key => (
                <option key={key} value={key}>{key}</option>
              ))}
            </Form.Select>
            <InputGroup.Text>
              GZIP
            </InputGroup.Text>
            <InputGroup.Checkbox
              id={'useGZIPInput'}
              type={'checkbox'}
              checked={useGZIP}
              onChange={e => setUseGZIP(e.target.checked)}
              disabled={isLoading}
            />
          </InputGroup>
        </Col>
      </Row>
      <Row className="mb-3">
        <Col md={1}>
          <Form.Label htmlFor="widthInput">Width</Form.Label>
        </Col>
        <Col md={5} className={'mb-2 mb-md-0'}>
          <InputGroup>
            <Form.Control
              id={'widthInput'}
              type={'number'}
              min={1}
              onChange={e => setWidth(+e.target.value)}
              disabled={isLoading || mode === Mode.DECODE}
            />
            <InputGroup.Text>px</InputGroup.Text>
          </InputGroup>
          <Form.Text>
            Optional value. If empty calculated for height or make square
          </Form.Text>
        </Col>
        <Col md={1}>
          <Form.Label htmlFor="heightInput">Height</Form.Label>
        </Col>
        <Col md={5}>
          <InputGroup>
            <Form.Control
              id={'heightInput'}
              type={'number'}
              min={1}
              onChange={e => setHeight(+e.target.value)}
              disabled={isLoading || mode === Mode.DECODE}
            />
            <InputGroup.Text>px</InputGroup.Text>
          </InputGroup>
          <Form.Text>
            Optional value. If empty calculated for width or make square
          </Form.Text>
        </Col>
      </Row>
      <Row className="mb-3 align-items-center">
        <Col md={2} lg={1}>
          <Form.Label htmlFor="fileNameInput">Filename</Form.Label>
        </Col>
        <Col md={10} lg={11}>
          <InputGroup>
            <Form.Control
              id={'fileNameInput'}
              type="text"
              value={filename}
              placeholder="Filename"
              onChange={(e) => setFilename(e.target.value)}
              disabled={isLoading}
            />
            <Button
              variant="primary"
              onClick={handleDownload}
              disabled={isLoading}
              title={'download'}
            >
              {isLoading ? <Spinner animation="border" size="sm" /> : <Download04Icon />}
            </Button>
          </InputGroup>
        </Col>
      </Row>
    </Container>
  );
};
