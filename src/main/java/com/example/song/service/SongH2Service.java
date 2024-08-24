package com.example.song.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import com.example.song.model.Song;
import com.example.song.repository.SongRepository;
import com.example.song.model.SongRowMapper;

@Service
public class SongH2Service implements SongRepository {

  @Autowired
  private JdbcTemplate db;

  @Override
  public ArrayList<Song> getSongs() {
    List<Song> songsCollection = db.query("SELECT * FROM playlist", new SongRowMapper());
    ArrayList<Song> songs = new ArrayList<>(songsCollection);
    return songs;
  };

  @Override
  public Song getSongById(int songId) {
    try {
      Song song = db.queryForObject("SELECT * FROM playlist WHERE songId=?", new SongRowMapper(), songId);
      return song;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

  }

  @Override
  public Song addSong(Song song) {
    db.update("INSERT INTO playlist(songName, lyricist, singer, musicDirector)  VALUES (?, ?, ?, ?)",
        song.getSongName(), song.getLyricist(), song.getSinger(), song.getMusicDirector());

    return db.queryForObject("SELECT * FROM playlist WHERE songName=? and lyricist=?", new SongRowMapper(),
        song.getSongName(), song.getLyricist());

  }

  @Override
  
    public Song updateSong(int songId, Song song) {
        if (song.getSongName() != null) {
            db.update("update playlist set songName = ? where songId =?", song.getSongName(), songId);
        }
        if (song.getLyricist() != null) {
            db.update("update playlist set lyricist = ? where songId =?", song.getLyricist(), songId);
        }
        if (song.getSinger() != null) {
            db.update("update playlist set singer = ? where songId =?", song.getSinger(), songId);
        }
        if (song.getMusicDirector() != null) {
            db.update("update playlist set singer = ? where songId =?", song.getSinger(), songId);
        }
        return getSongById(songId);
    }

  }

  @Override
  public void deleteSong(int songId) {
    db.update("DELETE FROM playlist WHERE songId=?", songId);

  }
}