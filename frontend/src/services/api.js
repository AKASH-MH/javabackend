import axios from 'axios';

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const jobService = {
  getAll: () => api.get('/jobs'),
  getById: (id) => api.get(`/jobs/${id}`),
};

export const candidateService = {
  create: (data) => api.post('/candidates', data),
};

export const applicationService = {
  apply: (data) => api.post('/applications', data),
  getByCandidate: (candidateId) => api.get(`/applications/candidate/${candidateId}`),
};

export default api;
