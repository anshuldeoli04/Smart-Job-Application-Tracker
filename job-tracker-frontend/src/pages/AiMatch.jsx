import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../services/Api';
import * as pdfjsLib from 'pdfjs-dist';

pdfjsLib.GlobalWorkerOptions.workerSrc = '/pdf.worker.min.js';

export default function AiMatch() {
  const navigate = useNavigate();
  const [jobs, setJobs] = useState([]);
  const [selectedJob, setSelectedJob] = useState('');
  const [resumeText, setResumeText] = useState('');
  const [fileName, setFileName] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [extracting, setExtracting] = useState(false);
  const fileRef = useRef();

  useEffect(() => {
    API.get('/jobs').then(res => setJobs(res.data));
  }, []);

  const handleFileUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    setFileName(file.name);
    setExtracting(true);
    setResumeText('');

    try {
      if (file.type === 'application/pdf') {
        // PDF extract
        const arrayBuffer = await file.arrayBuffer();
        const pdf = await pdfjsLib.getDocument({ data: arrayBuffer }).promise;
        let text = '';
        for (let i = 1; i <= pdf.numPages; i++) {
          const page = await pdf.getPage(i);
          const content = await page.getTextContent();
          text += content.items.map(item => item.str).join(' ') + '\n';
        }
        setResumeText(text.trim());
      } else if (
        file.name.endsWith('.docx') ||
        file.type === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
      ) {
        // DOCX extract
        const mammoth = await import('mammoth');
        const arrayBuffer = await file.arrayBuffer();
        const result = await mammoth.extractRawText({ arrayBuffer });
        setResumeText(result.value.trim());
      } else {
        alert('Only PDF or DOCX files supported');
        setFileName('');
      }
    } catch (err) {
      alert('Could not read file. Try a different file.');
      setFileName('');
    } finally {
      setExtracting(false);
    }
  };

  const handleAnalyze = async () => {
    if (!selectedJob || !resumeText.trim()) {
      alert('Please select a job and upload your resume');
      return;
    }
    setLoading(true);
    setResult(null);
    try {
      const res = await API.post('/ai/match', {
        applicationId: selectedJob,
        resumeText: resumeText,
      });
      setResult(res.data);
    } catch {
      alert('AI analysis failed. Check if backend is running.');
    } finally {
      setLoading(false);
    }
  };

  const scoreColor = (score) => {
    if (score >= 70) return 'text-green-600';
    if (score >= 40) return 'text-yellow-600';
    return 'text-red-500';
  };

  const scoreBarColor = (score) => {
    if (score >= 70) return 'bg-green-500';
    if (score >= 40) return 'bg-yellow-500';
    return 'bg-red-500';
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white border-b border-gray-200 px-6 py-4 flex justify-between items-center">
        <h1 className="text-lg font-semibold text-gray-800">Job Tracker</h1>
        <button onClick={() => navigate('/dashboard')} className="text-sm text-gray-500 hover:text-gray-700">
          ← Back to Dashboard
        </button>
      </nav>

      <div className="max-w-3xl mx-auto px-6 py-8">
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-8 mb-6">
          <h2 className="text-xl font-semibold text-gray-800 mb-1">AI Resume Match</h2>
          <p className="text-sm text-gray-500 mb-6">
            Upload your resume — AI will score how well you match the job
          </p>

          <div className="space-y-5">
            {/* Job Select */}
            <div>
              <label className="text-sm font-medium text-gray-700">Select Job Application</label>
              <select
                value={selectedJob}
                onChange={(e) => setSelectedJob(e.target.value)}
                className="mt-1 w-full border border-gray-200 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">-- Select a job --</option>
                {jobs.map(job => (
                  <option key={job.id} value={job.id}>
                    {job.companyName} — {job.role}
                  </option>
                ))}
              </select>
            </div>

            {/* File Upload */}
            <div>
              <label className="text-sm font-medium text-gray-700">Upload Resume</label>
              <div
                onClick={() => fileRef.current.click()}
                className="mt-1 w-full border-2 border-dashed border-gray-200 rounded-lg px-4 py-8 text-center cursor-pointer hover:border-blue-400 hover:bg-blue-50 transition"
              >
                {extracting ? (
                  <p className="text-blue-500 text-sm">Extracting text...</p>
                ) : fileName ? (
                  <div>
                    <p className="text-green-600 text-sm font-medium">✓ {fileName}</p>
                    <p className="text-gray-400 text-xs mt-1">
                      {resumeText.length} characters extracted
                    </p>
                  </div>
                ) : (
                  <div>
                    <p className="text-gray-500 text-sm">Click to upload PDF or DOCX</p>
                    <p className="text-gray-400 text-xs mt-1">Supported: .pdf, .docx</p>
                  </div>
                )}
                <input
                  ref={fileRef}
                  type="file"
                  accept=".pdf,.docx"
                  onChange={handleFileUpload}
                  className="hidden"
                />
              </div>
            </div>

            {/* Extracted text preview */}
            {resumeText && (
              <div className="bg-gray-50 rounded-lg p-4 max-h-32 overflow-y-auto">
                <p className="text-xs text-gray-400 mb-1">Extracted text preview:</p>
                <p className="text-xs text-gray-600 line-clamp-4">{resumeText.slice(0, 300)}...</p>
              </div>
            )}

            <button
              onClick={handleAnalyze}
              disabled={loading || extracting || !resumeText}
              className="w-full bg-blue-600 text-white py-2.5 rounded-lg text-sm font-medium hover:bg-blue-700 transition disabled:opacity-50"
            >
              {loading ? 'Analyzing with AI...' : 'Analyze Match'}
            </button>
          </div>
        </div>

        {/* Result */}
        {result && (
          <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-8">
            <h3 className="text-lg font-semibold text-gray-800 mb-6">Analysis Result</h3>

            <div className="text-center mb-8">
              <p className={`text-6xl font-bold ${scoreColor(result.matchScore)}`}>
                {result.matchScore}%
              </p>
              <p className="text-gray-500 text-sm mt-2">Match Score</p>
              <div className="mt-4 h-3 bg-gray-100 rounded-full max-w-sm mx-auto">
                <div
                  className={`h-3 rounded-full transition-all ${scoreBarColor(result.matchScore)}`}
                  style={{ width: `${result.matchScore}%` }}
                />
              </div>
            </div>

            <div className="mb-6">
              <h4 className="text-sm font-medium text-gray-700 mb-3">Missing Skills</h4>
              <div className="flex flex-wrap gap-2">
                {JSON.parse(result.missingSkills || '[]').map((skill, i) => (
                  <span key={i} className="bg-red-50 text-red-600 text-xs px-3 py-1.5 rounded-full">
                    {skill}
                  </span>
                ))}
              </div>
            </div>

            <div>
              <h4 className="text-sm font-medium text-gray-700 mb-3">Suggestions</h4>
              <div className="space-y-2">
                {JSON.parse(result.suggestions || '[]').map((s, i) => (
                  <div key={i} className="flex gap-3 items-start">
                    <span className="text-blue-500 mt-0.5">→</span>
                    <p className="text-sm text-gray-600">{s}</p>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}