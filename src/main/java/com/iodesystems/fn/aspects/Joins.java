package com.iodesystems.fn.aspects;

import com.iodesystems.fn.data.From;
import com.iodesystems.fn.data.Generator;
import com.iodesystems.fn.data.Pair;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Joins {

  public static <A, B> Iterable<Pair<A, B>> join(Iterable<A> as, Iterable<B> bs) {
    return Iterables.flatten(
        Iterables.multiply(as, new From<A, Iterable<Pair<A, B>>>() {
          @Override
          public Iterable<Pair<A, B>> from(A a) {
            return Iterables.convert(bs, new From<B, Pair<A, B>>() {
              @Override
              public Pair<A, B> from(B b) {
                return Pair.of(a, b);
              }
            });
          }
        }));
  }

  public static <A, B> Iterable<Pair<A, B>> join(
      Iterable<A> as, From<A, B> aIndexer, Iterable<B> bs) {
    Map<B, A> index = Indexes.index(as, aIndexer);
    return Iterables.convert(
        Iterables.where(bs, index::containsKey), new From<B, Pair<A, B>>() {
          @Override
          public Pair<A, B> from(B b) {
            return Pair.of(index.get(b), b);
          }
        });
  }

  public static <A, B> Iterable<Pair<A, B>> leftJoin(
      Iterable<A> as, From<A, B> aIndexer, Iterable<B> bs) {
    Map<B, A> index = Indexes.index(as, aIndexer);
    return Iterables.convert(bs, new From<B, Pair<A, B>>() {
      @Override
      public Pair<A, B> from(B b) {
        return Pair.of(index.get(b), b);
      }
    });
  }

  public static <A, B, C> Iterable<Pair<A, B>> join(
      Iterable<A> as, From<A, C> aIndexer, Iterable<B> bs, From<B, C> bIndexer) {
    Map<C, List<A>> index = Groups.group(as, aIndexer);
    return Iterables.flatten(
        Iterables.multiply(
            bs,
            new From<B, Iterable<Pair<A, B>>>() {
              @Override
              public Iterable<Pair<A, B>> from(B b) {
                return Iterables.convert(
                    Maps.getOrAdd(index, bIndexer.from(b), Collections::emptyList),
                    new From<A, Pair<A, B>>() {
                      @Override
                      public Pair<A, B> from(A a) {
                        return Pair.of(a, b);
                      }
                    });
              }
            }));
  }

  public static <A, B, C> Iterable<Pair<A, B>> leftJoin(
      Iterable<A> as, From<A, C> aIndexer, Iterable<B> bs, From<B, C> bIndexer) {
    Map<C, List<A>> index = Groups.group(as, aIndexer);
    List<A> singleEmpty = Collections.singletonList(null);
    return Iterables.flatten(
        Iterables.multiply(
            bs,
            new From<B, Iterable<Pair<A, B>>>() {
              @Override
              public Iterable<Pair<A, B>> from(B b) {
                return Iterables.convert(
                    Maps.getOrAdd(index, bIndexer.from(b), new Generator<List<A>>() {
                      @Override
                      public List<A> next() {
                        return singleEmpty;
                      }
                    }),
                    new From<A, Pair<A, B>>() {
                      @Override
                      public Pair<A, B> from(A a) {
                        return Pair.of(a, b);
                      }
                    });
              }
            }));
  }
}
