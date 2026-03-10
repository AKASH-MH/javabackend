import React, { useState, useEffect } from 'react';
import { jobService, candidateService, applicationService } from './services/api';
import { 
  Briefcase, User, Send, ChevronRight, MapPin, 
  Building2, Clock, Sparkles, PlusCircle, CheckCircle
} from 'lucide-react';

function App() {
  const [jobs, setJobs] = useState([]);
  const [candidate, setCandidate] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showStatus, setShowStatus] = useState(null);
  const [formData, setFormData] = useState({ name: '', email: '', phone: '', skills: '', resume: '' });

  useEffect(() => {
    fetchJobs();
  }, []);

  const fetchJobs = async () => {
    try {
      const res = await jobService.getAll();
      setJobs(res.data.content || []);
    } catch (err) {
      console.error("Failed to fetch jobs", err);
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const res = await candidateService.create(formData);
      setCandidate(res.data);
      triggerStatus('success', 'Profile Created Successfully');
    } catch (err) {
      triggerStatus('error', err.response?.data?.message || 'Registration failed');
    }
  };

  const handleApply = async (jobId) => {
    if (!candidate) {
      document.getElementById('register').scrollIntoView({ behavior: 'smooth' });
      triggerStatus('error', 'Please register first to apply');
      return;
    }
    try {
      await applicationService.apply({ candidateId: candidate.id, jobOpeningId: jobId });
      triggerStatus('success', 'Application Sent!');
    } catch (err) {
      triggerStatus('error', err.response?.data?.message || 'Apply failed');
    }
  };

  const triggerStatus = (type, message) => {
    setShowStatus({ type, message });
    setTimeout(() => setShowStatus(null), 4000);
  };

  return (
    <div className="container">
      {/* Toast Notification */}
      {showStatus && (
        <div className={`glass animate-reveal`} style={{
          position: 'fixed', top: '2rem', right: '2rem', padding: '1.25rem 2rem', borderRadius: '16px',
          zIndex: 2000, color: 'white', display: 'flex', alignItems: 'center', gap: '0.75rem',
          backgroundColor: showStatus.type === 'success' ? 'rgba(16, 185, 129, 0.9)' : 'rgba(239, 68, 68, 0.9)',
          border: '1px solid rgba(255,255,255,0.2)'
        }}>
          {showStatus.type === 'success' ? <CheckCircle size={20}/> : <Sparkles size={20}/>}
          <span style={{fontWeight: 600}}>{showStatus.message}</span>
        </div>
      )}

      {/* Navigation */}
      <nav className="navbar glass">
        <div className="nav-logo">JobPortal.io</div>
        <div style={{display: 'flex', gap: '1.5rem', alignItems: 'center'}}>
          {candidate ? (
            <div className="glass" style={{padding: '0.5rem 1rem', borderRadius: '100px', display: 'flex', alignItems: 'center', gap: '0.75rem'}}>
              <div style={{width: 28, height: 28, borderRadius: '50%', background: 'linear-gradient(to bottom right, var(--primary), var(--secondary))', border: '2px solid white'}} />
              <span style={{fontSize: '0.9rem', fontWeight: 600}}>Hello, {candidate.name.split(' ')[0]}</span>
            </div>
          ) : (
            <button className="primary" onClick={() => document.getElementById('register').scrollIntoView({ behavior: 'smooth' })}>
              Join as Talent
            </button>
          )}
        </div>
      </nav>

      {/* Hero */}
      <section style={{padding: '6rem 0', textAlign: 'center'}} className="animate-reveal">
        <div className="job-badge" style={{marginBottom: '1.5rem'}}>#1 Talent Marketplace</div>
        <h1 style={{fontSize: '4.5rem', fontWeight: 900, lineHeight: 1.1, marginBottom: '1.5rem', letterSpacing: '-0.03em'}}>
          Unlock Your <span style={{background: 'linear-gradient(to right, #8b5cf6, #ec4899)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent'}}>Future</span> Career
        </h1>
        <p style={{fontSize: '1.25rem', color: 'var(--text-muted)', maxWidth: '650px', margin: '0 auto'}}>
          Connect with world-class engineering teams. Our platform makes your next career move seamless, fast, and exciting.
        </p>
      </section>

      {/* Jobs Section */}
      <main style={{marginTop: '2rem'}}>
        <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: '3rem'}}>
          <div>
            <h2 style={{fontSize: '2rem', display: 'flex', alignItems: 'center', gap: '0.75rem'}}>
              <Briefcase size={32} className="text-primary" /> Active Openings
            </h2>
            <p style={{color: 'var(--text-muted)'}}>Hand-picked opportunities for top engineers</p>
          </div>
          <div className="glass" style={{padding: '0.5rem 1rem', borderRadius: '12px', fontSize: '0.9rem'}}>
            <span style={{color: 'var(--success)', fontWeight: 700}}>●</span> {jobs.length} roles online
          </div>
        </div>

        {loading ? (
          <div className="job-grid">
            {[1,2,3].map(n => <div key={n} className="job-card glass" style={{height: '300px', opacity: 0.5}} />)}
          </div>
        ) : (
          <div className="job-grid">
            {jobs.map((job, idx) => (
              <div key={job.id} className="job-card glass animate-reveal" style={{animationDelay: `${idx * 0.1}s`}}>
                <div className="job-meta">
                  <span className="job-badge">{job.employmentType}</span>
                </div>
                <h3>{job.title}</h3>
                <div style={{display: 'flex', flexDirection: 'column', gap: '0.75rem', marginTop: '0.5rem'}}>
                  <div style={{display: 'flex', alignItems: 'center', gap: '0.6rem', color: 'var(--text-muted)', fontSize: '0.9rem'}}>
                    <Building2 size={16} color="var(--primary)" /> {job.department || 'Engineering'}
                  </div>
                  <div style={{display: 'flex', alignItems: 'center', gap: '0.6rem', color: 'var(--text-muted)', fontSize: '0.9rem'}}>
                    <MapPin size={16} color="var(--primary)" /> {job.location}
                  </div>
                  <div style={{display: 'flex', alignItems: 'center', gap: '0.6rem', color: 'var(--text-muted)', fontSize: '0.9rem'}}>
                    <Clock size={16} color="var(--primary)" /> Posted {job.postedDate}
                  </div>
                </div>
                <p style={{marginTop: '1.5rem', color: 'var(--text-muted)', fontSize: '0.95rem', flexGrow: 1}}>
                  {job.description?.length > 120 ? job.description.substring(0, 120) + '...' : job.description}
                </p>
                <div style={{marginTop: '2rem', paddingTop: '1.5rem', borderTop: '1px solid var(--border)'}}>
                  <button className="primary" style={{width: '100%'}} onClick={() => handleApply(job.id)}>
                    Apply Now <ChevronRight size={18} />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>

      {/* Registration Section */}
      {!candidate && (
        <section id="register" className="animate-reveal" style={{animationDelay: '0.4s'}}>
          <div className="form-section glass">
            <div style={{textAlign: 'center', marginBottom: '3rem'}}>
              <div style={{width: 64, height: 64, background: 'rgba(139, 92, 246, 0.1)', borderRadius: '16px', display: 'grid', placeItems: 'center', margin: '0 auto 1.5rem'}}>
                <PlusCircle size={32} color="var(--primary)" />
              </div>
              <h2 style={{fontSize: '2rem', marginBottom: '0.5rem'}}>Elevate Your Profile</h2>
              <p style={{color: 'var(--text-muted)'}}>Join our talent pool to start applying for jobs instantly.</p>
            </div>
            <form onSubmit={handleRegister}>
              <div style={{display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem'}} className="form-group">
                <div>
                  <label>Full Name</label>
                  <input required placeholder="Elon Musk" value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} />
                </div>
                <div>
                  <label>Email Address</label>
                  <input required type="email" placeholder="elon@spacex.com" value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} />
                </div>
              </div>
              <div style={{display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem'}} className="form-group">
                <div>
                  <label>Phone Number</label>
                  <input placeholder="+1 234 567 890" value={formData.phone} onChange={e => setFormData({...formData, phone: e.target.value})} />
                </div>
                <div>
                  <label>Key Skills</label>
                  <input placeholder="React, Node, Java" value={formData.skills} onChange={e => setFormData({...formData, skills: e.target.value})} />
                </div>
              </div>
              <div className="form-group">
                <label>Tell us about yourself</label>
                <textarea rows="4" placeholder="Brief bio or links to your portfolio/resume..." value={formData.resume} onChange={e => setFormData({...formData, resume: e.target.value})} />
              </div>
              <button type="submit" className="primary" style={{width: '100%', height: '56px', fontSize: '1.1rem'}}>
                <Send size={18} /> Create My Talent Profile
              </button>
            </form>
          </div>
        </section>
      )}

      {/* Footer */}
      <footer style={{marginTop: '10rem', padding: '4rem 0', textAlign: 'center', borderTop: '1px solid var(--border)'}}>
        <div className="nav-logo" style={{marginBottom: '1.5rem', fontSize: '1.5rem'}}>JobPortal.io</div>
        <div style={{display: 'flex', gap: '2rem', justifyContent: 'center', marginBottom: '2rem', color: 'var(--text-muted)', fontSize: '0.9rem'}}>
          <a href="#">About</a>
          <a href="#">Privacy</a>
          <a href="#">Terms</a>
          <a href="#">Contact</a>
        </div>
        <p style={{color: 'var(--text-muted)', fontSize: '0.8rem'}}>
          &copy; 2024 Ultimate Job Marketplace. Designed for the bold.
        </p>
      </footer>
    </div>
  );
}

export default App;
